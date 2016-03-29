import java.util.Date;
import java.util.Comparator;

public class TodoItem {
    public enum Status { TODO, DONE }; //need to handle floating
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

    /*
     * GETTERS
     */	
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

    /*
     * SETTERS
     */
    public void setPriority (int level){
        this.priority = level;
    }
    public void setDueDate(Date date){
    	this.dueDate = date;
    }
    public void setContents(String newContents){
    	this.contents = newContents;
    }
    protected void setStatus(Status stat){
    	this.stat=stat;
    }
    public void markDone(){
    	switch(stat){
    	case TODO:
    		stat = Status.DONE;
    		break;
   		default:
   			break;
    	}
    }
    
    public void markUndone(){
    	switch(stat){
    	case DONE:
    		stat = Status.TODO;
    		break;
   		default:
   			break;
    	}
    }
    
    /*
     * Convert TodoItem to String
     */
    public String toString() {
        return
            ((this.stat == Status.TODO ? "TODO|": "DONE|") +
            (priority) + "|" +
            (dueDate) + "|" +
            (contents));
    }
    
    /*
     * Comparators for sorting
    */
    public static Comparator<TodoItem> getDateComparator () {
        return new Comparator<TodoItem> () {
            public int compare (TodoItem i, TodoItem j) {
                return i.getDueDate().compareTo(j.getDueDate());
            }
        };
    }
    public static Comparator<TodoItem> getPriorityComparator () {
        return new Comparator<TodoItem> () {
            public int compare (TodoItem i, TodoItem j) {
                return Integer.compare(i.getPriority(), j.getPriority());
            }
        };
    }
    public static Comparator<TodoItem> getStatusComparator () {
        return new Comparator<TodoItem> () {
            public int compare (TodoItem i, TodoItem j) {
                if (i.getStatus() == Status.TODO) {
                    if (j.getStatus () == Status.TODO) {
                        return 0;
                    }
                    return 1;
                } else {
                    if (j.getStatus() == Status.TODO) {
                        return -1;
                    }
                    return 0;
                }
            }
        };
    }
    public static Comparator<TodoItem> getContentsComparator () {
        return new Comparator<TodoItem> () {
            public int compare (TodoItem i, TodoItem j) {
                return i.getContents().compareTo(j.getContents());
            }
        };
    }
}
