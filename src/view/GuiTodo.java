package view;

import java.util.Comparator;

import model.TodoItem;
public class GuiTodo {
	public enum SoD {
		START, DUE, FLOATING
	};
	private SoD status;
	private TodoItem todo;
	
	public GuiTodo(TodoItem todo, SoD status) {
		this.todo = todo;
		this.status = status;
	}
	public SoD getStatus() {
		return status;
	}
	public TodoItem getTodo() {
		return todo;
	}
	public static Comparator<GuiTodo> getPriorityComparator() {
		return new Comparator<GuiTodo>() {
			public int compare(GuiTodo i, GuiTodo j) {
				return TodoItem.getPriorityComparator().compare(i.getTodo(), j.getTodo());
			}
		};
	}
	public static Comparator<GuiTodo> getContentsComparator() {
		return new Comparator<GuiTodo>() {
			public int compare(GuiTodo i, GuiTodo j) {
				return TodoItem.getContentsComparator().compare(i.getTodo(), j.getTodo());
			}
		};
	}
	public static Comparator<GuiTodo> getStatusComparator() {
		return new Comparator<GuiTodo>() {
			public int compare(GuiTodo i, GuiTodo j) {
				return TodoItem.getStatusComparator().compare(i.getTodo(), j.getTodo());
			}
		};
	}
	public static Comparator<GuiTodo> getStartDateComparator() {
		return new Comparator<GuiTodo>() {
			public int compare(GuiTodo i, GuiTodo j) {
				return TodoItem.getStartDateComparator().compare(i.getTodo(), j.getTodo());
			}
		};
	}
	public static Comparator<GuiTodo> getDueDateComparator() {
		return new Comparator<GuiTodo>() {
			public int compare(GuiTodo i, GuiTodo j) {
				return TodoItem.getDueDateComparator().compare(i.getTodo(), j.getTodo());
			}
		};
	}

}
