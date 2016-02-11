import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoFile {
    private String path;
    private List<TodoItem> todos;
    int line; //line is indexed at 1

    public TodoFile (String path) {
        this.path = path;
        this.todos = new ArrayList<TodoItem>();
        this.line = 1;
    }

    public void readTodos () throws IOException {
        todos = new ArrayList<TodoItem>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            todos.add(parseTodo(reader.readLine()));
            line++;
        } finally {
            reader.close();
        }
    }

    private TodoItem parseTodo (String todo) {
        String[] parts = todo.split("|");
        if (parts.length < 4) {
            error("Not enough sections", line);
        }

        TodoItem.Status s = null;
        if (parts[0].equals("TODO")) {
            s = TodoItem.Status.TODO;
        } else if (parts[0].equals("DONE")) {
            s = TodoItem.Status.DONE;
        } else {
            error("Status is incorrect format", line);
        }

        int p = 0; //support optional
        try {
            p = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            error("Priority is not a valid int", line);
        }

        //TODO Date format
        //TODO Date is optional
        Date d = null;

        return new TodoItem(s, p, d, parts[3]);
    }

    private void error (String msg, int l) {
        System.err.print(msg + " at line " + l);
        System.exit(1);
    }
}
