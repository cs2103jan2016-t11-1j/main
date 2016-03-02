import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;

public class TodoFile {
    private static final boolean APPEND_TO_FILE = true;
    private String fileName;
    private List<TodoItem> todos;
    int lines; //line is indexed at 1
    private PrintWriter writer;

    public TodoFile (String fileName) {
        this.fileName = fileName;
        this.lines = 1;
        readTodos(fileName);
        try {
            this.writer = new PrintWriter(new FileWriter(fileName, APPEND_TO_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readTodos (String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                todos.add(parseTodo(currLine));
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TodoItem parseTodo (String todo) {
        String[] parts = todo.split("|");
        if (parts.length < 4) {
            error("Not enough sections", lines);
        }

        TodoItem.Status s = null;
        if (parts[0].equals("TODO")) {
            s = TodoItem.Status.TODO;
        } else if (parts[0].equals("DONE")) {
            s = TodoItem.Status.DONE;
        } else {
            error("Status is incorrect format", lines);
        }

        int p = -1;
        try {
            p = parts[1].isEmpty() ? -1 : Integer.parseInt(parts[1]);
        } catch (Exception e) {
            error("Priority is not a valid int", lines);
        }

        Date d = null;

        try {
            d = parts[2].isEmpty() ? null : DateFormat.getDateInstance().parse(parts[2]);
        } catch (ParseException e) {
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

    public void searchDate(Date toFind) {
        for (TodoItem tdi: todos) {
            if (tdi.getDueDate().equals(toFind)) {
                System.out.println(tdi.getContents());
            }
        }
    }
    public void searchString(String toFind) {
        for (TodoItem tdi: todos) {
            if (tdi.getContents().equals(toFind)){
                System.out.println(tdi.getContents());
            }
        }
    }
    public void display() {
        if (this.isEmpty()) {
            System.out.println(fileName + "%s is empty");
            return;
        }
        printFile();
    }
    public void add (String rest) {
        //TODO make parser
        todos.add(rest);
        System.out.printf("added to %s: \"%s\"\n", fileName, rest);
    }
    private void printFile() {
        for (int i = 1; i < todos.size() + 1; i++) {
            printLine(i);
        }
    }
    
    private void printLine(int location) {
        System.out.printf("%d. %s\n", location, todos.get(location - 1));
    }
    public boolean isEmpty() {
        return this.todos.isEmpty();
    }
    public void write () {

    }

    public void exit() {
        write();
        writer.close();
    }
}
