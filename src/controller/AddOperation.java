package controller;

import model.TodoFile;
import model.TodoItem;

public class AddOperation extends Operation {
	private TodoItem addedItem;

	public AddOperation(TodoFile t, TodoItem i) {
		super(t);
		addedItem = i;
	}

	@Override
	public void execute() {
		super.todos.add(this.addedItem);
	}

	@Override
	public void inverse() {
		super.todos.delete(this.addedItem);
	}

}
