package model;
import java.io.*;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;

import model.TodoItem.Frequency;
import model.TodoItem.Status;

public class TodoFile {
    private static final String SPLITTER = "\\|";
    private static final DateFormat FORMATTER = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    private String fileName;
    private List<TodoItem> todos;
    private List<TodoItem> done;
    private int lines; //line is indexed at 1
    private PrintWriter writer = null;

    public TodoFile (String fileName) {
        this.fileName = fileName;
        this.lines = 1;
        this.todos = new ArrayList<TodoItem>();
        if (new File(fileName).exists()) {
            readTodos(fileName);
        }
    }

    public void readTodos (String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                TodoItem t = parseTodo(currLine);
                assert t != null;

                todos.add(t);
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TodoItem parseTodo (String todo) {
        String[] parts = todo.split(SPLITTER);
        if (parts.length != 5) {
            System.err.println(todo);
            System.err.println(parts[2]);
            System.err.println(parts.length);
            error("Wrong number of sections", lines);
        }

        TodoItem.Status s = null;
        if (parts[0].equals("TODO")) {
            s = TodoItem.Status.TODO;
        } else if (parts[0].equals("DONE")) {
            s = TodoItem.Status.DONE;
        } else {
            error("Status: " + parts[0] + " is incorrect format", lines);
        }

        int p = -1;
        try {
            p = parts[1].isEmpty() ? -1 : Integer.parseInt(parts[1]);
        } catch (Exception e) {
            error("Priority is not a valid int", lines);
        }

        Date d = null;

        try {
            d = parts[2].isEmpty() ? null : (Date)FORMATTER.parse(parts[2]);
        } catch (ParseException e) {
            e.printStackTrace();
            error("Error parsing date", lines);
        }

        return new TodoItem(s, p, d, null, parts[3], null);
    }

    private void error (String msg) {
        System.err.print(msg);
        System.exit(1);
    }

    private void error (String msg, int l) {
        System.err.print(msg + " at line " + l);
        System.exit(1);
    }
    public void delete (int index) {
        if (index < 1 || index > todos.size()) {
            System.out.printf("The index must be between 1 and %d, inclusive.\n", todos.size());
        } else {
            System.out.printf("deleted from %s: \"%s\"\n", fileName, todos.remove(index - 1).toString());
        }
    }
    
    public void delete (TodoItem t) {
        todos.remove(t);
    }

    public void searchDate(Date toFind) {
        TodoSearcher tds = new TodoSearcher();
        tds.searchDate(todos, toFind);
    }
    public void searchString(String toFind) {
        TodoSearcher tds = new TodoSearcher();
        tds.searchString(todos, toFind);
    }
    public void searchTime(Date toFind){
        TodoSearcher tds = new TodoSearcher();
        tds.searchTime(todos, toFind);
    }

    public void display() {
        if (this.isEmpty()) {
            System.out.println(fileName + " is empty");
            return;
        }
        printFile();
    }
    
    public void displayDone() {
        if (done.isEmpty()) {
            System.out.println(fileName + " has no done items");
            return;
        }
        printDoneItems();
    }

    /**
     * Adds a TodoItem to the <tt>todos</tt> List with
     * the parameter, <tt>message</tt>, as the content.
     * @param message that the user wants to store
     */
    public void add (String message){
        todos.add(new TodoItem(TodoItem.Status.TODO, -1, null, null, message, Frequency.NONE));
        System.out.printf("added to %s: \"%s\"\n", fileName, message);
    }
    
    public void add (int priority, String message){
        todos.add(new TodoItem(TodoItem.Status.TODO, priority, null, null, message, Frequency.NONE));
        System.out.printf("added to %s: \"%s\"\n", fileName, message);
    }
    
    public void add (int priority, Date startDate, String message){
        todos.add(new TodoItem(TodoItem.Status.TODO, priority, startDate, null, message, Frequency.NONE));
        System.out.printf("added to %s: \"%s\"\n", fileName, message);
    }
    
    public void add (int priority, String message, Date dueDate){
        todos.add(new TodoItem(TodoItem.Status.TODO, priority, null, dueDate, message, Frequency.NONE));
        System.out.printf("added to %s: \"%s\"\n", fileName, message);
    }
    
    public void add (int priority, Date startDate, Date dueDate, String message){
        todos.add(new TodoItem(TodoItem.Status.TODO, priority, startDate, dueDate, message, Frequency.NONE));
        System.out.printf("added to %s: \"%s\"\n", fileName, message);
    }
    
    public void add(TodoItem addedItem) {
    	todos.add(addedItem);
    	System.out.printf("added to %s: \"%s\"\n", fileName, addedItem.getContents());
	}
    
    public void printFile() {
        for (int i = 1; i < todos.size() + 1; i++) {
            printLine(i);
        }
    }
    
    public void printDoneItems() {
        for (int i = 1; i < done.size() + 1; i++) {
            printLine(i);
        }
    }
    
    public TodoItem getItem(int i) {
        return this.todos.get(i);
    }

    private void printLine(int location) {
        System.out.printf("%d. %s\n", location, todos.get(location - 1));
    }
    public boolean isEmpty() {
        return this.todos.isEmpty();
    }
    public void write () {
        try {
            this.writer = new PrintWriter(new FileWriter(fileName, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = todos.size(); i > 0; i--) {
            writer.println(todos.remove(0));
        }
        writer.flush();
        System.out.printf("Wrote to %s.\n", this.fileName);
    }
    public void clear () {
        todos.clear();
    }

    public void exit() {
        write();
        writer.close();
    }
    
    public void sort() {
        Collections.sort(todos, TodoItem.getDueDateComparator());
    }
    public void sortByDueDate() {
        Collections.sort(todos, TodoItem.getDueDateComparator());
    }
    public void sortByStartDate() {
        Collections.sort(todos, TodoItem.getStartDateComparator());
    }
    public void sortByPriority() {
        Collections.sort(todos, TodoItem.getPriorityComparator());
    }
    public void sortByStatus() {
        Collections.sort(todos, TodoItem.getStatusComparator());
    }
    public void sortByContents() {
        Collections.sort(todos, TodoItem.getContentsComparator());
    }
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (TodoItem item : todos) {
            ret.append(item.toString());
            ret.append("\n");
        }
        return ret.toString();
    }

    public void markDone(TodoItem tdi){
    	TodoItem replacement = new TodoItem(tdi.getStatus(),tdi.getPriority(),tdi.getStartDate(),tdi.getDueDate(),tdi.getContents(),Frequency.NONE);
    	switch(tdi.getFreq()){
        case DAILY:
        	tdi.getDueDate().setDate(tdi.getDueDate().getDate()+1);
            break;
        case WEEKLY:
            tdi.getDueDate().setDate(tdi.getDueDate().getDate()+7);
            break;
        case MONTHLY:
            tdi.getDueDate().setMonth(tdi.getDueDate().getMonth()+1);
            break;
        case YEARLY:
            tdi.getDueDate().setYear(tdi.getDueDate().getYear()+1);
            break;
		default:
			tdi.markDone();
	        todos.remove(tdi);
			break;
        }
    	done.add(replacement);
    }
    public void markUndone(TodoItem tdi){
    	tdi.markUndone();
    	todos.add(tdi);
		done.remove(tdi);
    }
    public void toggle(TodoItem tdi){
    	if (tdi.getStatus()==Status.DONE){
    		tdi.markUndone();
    		todos.add(tdi);
    		done.remove(tdi);
    	}else{
    		tdi.markDone();
    		todos.remove(tdi);
    		done.add(tdi);
    	}
    }
    
    public void updateDueDate(TodoItem tdi, Date newDate){
    	tdi.setDueDate(newDate);
    }
    public void updateStartDate(TodoItem tdi, Date newDate){
    	tdi.setStartDate(newDate);
    }
    public void updatePriority(TodoItem tdi, int newPriority){
    	tdi.setPriority(newPriority);
    }
    public void updateFrequency(TodoItem tdi, Frequency newFreq){
    	tdi.setFreq(newFreq);
    }
    public void updateContents(TodoItem tdi, String newContents){
    	tdi.setContents(newContents);
    }

	
}
