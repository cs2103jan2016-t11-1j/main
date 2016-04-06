package model;

import java.util.Date;
import java.util.Comparator;

public class TodoItem {
	public enum Status {
		TODO, DONE
	}; // need to handle floating

	private Status status;
	private int priority; // -1 if not set
	private Date startDate; // possibly null
	private Date dueDate; // possibly null
	private String contents;

	public enum Frequency {
		NONE, DAILY, WEEKLY, MONTHLY, YEARLY
	};

	private Frequency freq; // for recurring tasks

	public TodoItem(String contents) {
		this.status = Status.TODO;
		this.priority = -1;
		this.startDate = null;
		this.dueDate = null;
		this.contents = contents;
	}
	public TodoItem(Status stat, int priority, Date startDate, Date dueDate, String contents, Frequency freq) {
		this.status = stat;
		this.priority = priority;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.contents = contents;
		this.freq = freq;
	}

	/*
	 * GETTERS
	 */
	public Status getStatus() {
		return status;
	}

	public int getPriority() {
		return priority;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public String getContents() {
		return contents;
	}

	public Frequency getFreq() {
		return freq;
	}

	/*
	 * SETTERS
	 */
	public void setPriority(int level) {
		this.priority = level;
	}

	public void setDueDate(Date date) {
		this.dueDate = date;
	}

	public void setStartDate(Date date) {
		this.startDate = date;
	}

	public void setContents(String newContents) {
		this.contents = newContents;
	}

	private void setStatus(Status stat) {
		this.status = stat;
	}

	public void setFreq(Frequency freq) {
		this.freq = freq;
	}

	public void toggleStatus() {
		switch (status) {
		case TODO:
			setStatus(Status.DONE);
			break;
		case DONE:
			setStatus(Status.TODO);
			break;
		default:
			break;
		}
	}

	public void markDone() {
		switch (status) {
		case TODO:
			setStatus(Status.DONE);
			break;
		default:
			break;
		}
	}

	public void markUndone() {
		switch (status) {
		case DONE:
			setStatus(Status.TODO);
			break;
		default:
			break;
		}
	}

	/*
	 * Convert TodoItem to String
	 * 
	 * public String toString() { return (this.stat == Status.TODO ? "TODO|":
	 * "DONE|") + (priority == -1 ? "" : Integer.toString(priority)) + "|" +
	 * (dueDate != null ? dueDate.toString() : "") + "|" + contents + " |"; }
	 */
	public String toString() {
		String todo = "";
		todo += getStatus() + "|";
		todo += (getPriority() == -1 ? "" : getPriority()) + "|";
		todo += (getStartDate() != null ? getStartDate().toString() : "") + "|";
		todo += (getDueDate() != null ? getDueDate().toString() : "") + "|";
		todo += (getFreq() == Frequency.NONE ? "" : getFreq()) + "|";
		todo += getContents();
		return todo;
	}

	/*
	 * Comparators for sorting
	 */
	public static Comparator<TodoItem> getDueDateComparator() {
		return new Comparator<TodoItem>() {
			public int compare(TodoItem i, TodoItem j) {
				return i.getDueDate().compareTo(j.getDueDate());
			}
		};
	}

	public static Comparator<TodoItem> getStartDateComparator() {
		return new Comparator<TodoItem>() {
			public int compare(TodoItem i, TodoItem j) {
				return i.getStartDate().compareTo(j.getStartDate());
			}
		};
	}

	public static Comparator<TodoItem> getPriorityComparator() {
		return new Comparator<TodoItem>() {
			public int compare(TodoItem i, TodoItem j) {
				return Integer.compare(i.getPriority(), j.getPriority());
			}
		};
	}

	public static Comparator<TodoItem> getStatusComparator() {
		return new Comparator<TodoItem>() {
			public int compare(TodoItem i, TodoItem j) {
				if (i.getStatus() == Status.TODO) {
					if (j.getStatus() == Status.TODO) {
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

	public static Comparator<TodoItem> getContentsComparator() {
		return new Comparator<TodoItem>() {
			public int compare(TodoItem i, TodoItem j) {
				return i.getContents().compareTo(j.getContents());
			}
		};
	}

	public boolean isDone() {
		return status == Status.DONE;
	}

	public boolean isTodo() {
		return status == Status.TODO;
	}
}
