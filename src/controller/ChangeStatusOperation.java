package controller;
/**
 * @@author A0149108E
 */
import model.TodoFile;
import model.TodoItem;


public class ChangeStatusOperation extends Operation {
	TodoItem item;

	public ChangeStatusOperation(TodoFile t, TodoItem i) {
		super(t);
		item = i;
	}

	@Override
	public void execute() {
		todos.toggle(item);
	}

	@Override
	public void inverse() {
		todos.toggle(item);
	}

}
