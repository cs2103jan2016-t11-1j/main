package model;

import java.util.Date;
import java.util.Comparator;

public class TodoItem {
	/**
	 * @@author A0149108E
	 */
	public enum Status {
		TODO, DONE
	};

	/**
	 * @@author A0149108E
	 */
	private Status status;
	/**
	 * @@author A0135747X
	 */
	private int priority; // -1 if not set
	/**
	 * @@author A0135747X
	 */
	private Date startDate; // possibly null
	/**
	 * @@author A0149108E
	 */
	private Date dueDate; // possibly null
	/**
	 * @@author A0149108E
	 */
	private String contents;

	/**
	 * @@author A0135747X
	 */
	public enum Frequency {
		NONE, DAILY, WEEKLY, MONTHLY, YEARLY
	};

	private Frequency freq; // for recurring tasks

	/**
	 * @@author A0149108E and A0135747X
	 */
	public TodoItem(String contents) {
		this.status = Status.TODO;
		this.priority = -1;
		this.startDate = null;
		this.dueDate = null;
		this.contents = contents;
	}
	/**
	 * @@author A0149108E and A0135747X
	 */
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
	/**
	 * @@author A0135747X
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * @@author A0135747X
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @@author A0135747X
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @@author A0135747X
	 */
	public Date getDueDate() {
		return dueDate;
	}
	/**
	 * @@author A0135747X
	 */
	public String getContents() {
		return contents;
	}
	/**
	 * @@author A0135747X
	 */
	public Frequency getFreq() {
		return freq;
	}

	/*
	 * SETTERS
	 */
	/**
	 * @@author A0135747X
	 */
	public void setPriority(int level) {
		this.priority = level;
	}
	/**
	 * @@author A0135747X
	 */
	public void setDueDate(Date date) {
		this.dueDate = date;
	}
	/**
	 * @@author A0135747X
	 */
	public void setStartDate(Date date) {
		this.startDate = date;
	}
	/**
	 * @@author A0135747X
	 */
	public void setContents(String newContents) {
		this.contents = newContents;
	}
	/**
	 * @@author A0135747X
	 */
	private void setStatus(Status stat) {
		this.status = stat;
	}
	/**
	 * @@author A0135747X
	 */
	public void setFreq(Frequency freq) {
		this.freq = freq;
	}
	/**
	 * @@author A0135747X
	 */
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
	/**
	 * @@author A0135747X
	 */
	public void markDone() {
		switch (status) {
		case TODO:
			setStatus(Status.DONE);
			break;
		default:
			break;
		}
	}
	/**
	 * @@author A0135747X
	 */
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
	/**
	 * @@author A0149108E
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
	 *Comparators 
	 */
	/**
	 * @@author A0149108E
	 */
	public static Comparator<TodoItem> getDueDateComparator() {
		return new Comparator<TodoItem>() {
			public int compare(TodoItem i, TodoItem j) {
				if (i.getDueDate() == null || j.getDueDate() == null)
					return 0;
				return i.getDueDate().compareTo(j.getDueDate());
			}
		};
	}
	/**
	 * @@author A0149108E
	 */
	public static Comparator<TodoItem> getStartDateComparator() {
		return new Comparator<TodoItem>() {
			public int compare(TodoItem i, TodoItem j) {
				if (i.getStartDate() == null || j.getStartDate() == null)
					return 0;
				return i.getStartDate().compareTo(j.getStartDate());
			}
		};
	}
	/**
	 * @@author A0149108E
	 */
	public static Comparator<TodoItem> getPriorityComparator() {
		return new Comparator<TodoItem>() {
			public int compare(TodoItem i, TodoItem j) {
				return Integer.compare(i.getPriority(), j.getPriority());
			}
		};
	}
	/**
	 * @@author A0149108E
	 */
	public static Comparator<TodoItem> getStatusComparator() {
		return new Comparator<TodoItem>() {
			public int compare(TodoItem i, TodoItem j) {
				if (i.getStatus() == Status.TODO) {
					if (j.getStatus() == Status.TODO) {
						return 0;
					}
					return -1;
				} else {
					if (j.getStatus() == Status.TODO) {
						return 1;
					}
					return 0;
				}
			}
		};
	}
	/**
	 * @@author A0149108E
	 */
	public static Comparator<TodoItem> getContentsComparator() {
		return new Comparator<TodoItem>() {
			public int compare(TodoItem i, TodoItem j) {
				return i.getContents().compareTo(j.getContents());
			}
		};
	}
	
	/**
	 * @@author A0135747X
	 */
	public boolean isDone() {
		return status == Status.DONE;
	}
	/**
	 * @@author A0135747X
	 */
	public boolean isTodo() {
		return status == Status.TODO;
	}
}
