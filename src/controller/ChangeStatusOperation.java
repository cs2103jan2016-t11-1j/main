package controller;

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
		todos.changeStatus(item);
	}

	@Override
	public void inverse() {
		todos.changeStatus(item);
	}

}
