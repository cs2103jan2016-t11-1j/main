import java.util.Date;

public class TodoItem {
    public enum Status { TODO, DONE };
    private Status stat;
    private int priority; //-1 if not set
    private Date dueDate; //possibly null
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
    public String toString() {
        return contents;
        //TODO should be the format that goes to the file
    }
}
