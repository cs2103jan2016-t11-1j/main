import java.util.Date;

public enum Status { TODO, DONE };

public class TodoItem {
    private Status stat;
    private int priority;
    private Date dueDate;
    private String contents;

    public TodoItem (Status stat, int priority, Date dueDate, String contents) {
        this.stat = stat;
        this.priority = priority;
        this.dueDate = dueDate;
        this.contents = contents;
    }

    public Status getStatus() {
        return stat;
    }
    public int getPriority() {
        return priority;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public String getContents() {
        return contents;
    }
}
