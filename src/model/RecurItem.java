package model;
import java.util.Date;

public class RecurItem extends TodoItem {
	public enum Frequency{ DAILY, WEEKLY, MONTHLY, YEARLY };
	private Frequency freq;

	public RecurItem(Status stat, int priority, Date dueDate, String contents, Frequency freq) {
		super(stat, priority, dueDate, contents);
		this.freq = freq;
	}
	public RecurItem(TodoItem tdi, Frequency freq) {
		super(tdi.getStatus(), tdi.getPriority(), tdi.getDueDate(), tdi.getContents());
		this.freq = freq;
	}
	
	public Frequency getFreq(){
		return freq;
	}
}
