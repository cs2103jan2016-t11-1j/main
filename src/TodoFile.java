import java.io.*;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Pattern;

public class TodoFile {
    private static final String SPLITTER = "\\|";
    private static final DateFormat FORMATTER = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    private String fileName;
    private List<TodoItem> todos;
    int lines; //line is indexed at 1
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
        if (parts.length != 4) {
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

        return new TodoItem(s, p, d, parts[3]);
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
    public void add (String rest) {
        //TODO make parser
        todos.add(new TodoItem(TodoItem.Status.TODO, -1, new Date(), rest));
        System.out.printf("added to %s: \"%s\"\n", fileName, rest);
    }
    public void printFile() {
        for (int i = 1; i < todos.size() + 1; i++) {
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
        Collections.sort(todos, TodoItem.getDateComparator());
    }
    public void sortByDate() {
        Collections.sort(todos, TodoItem.getDateComparator());
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
}
