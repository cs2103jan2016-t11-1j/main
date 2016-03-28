package controller;

import model.TodoFile;
import model.TodoItem;

public class DeleteOperation extends Operation {
	private TodoItem deletedItem;

	public DeleteOperation(TodoFile t, TodoItem deletedItem) {
		super(t);
		this.deletedItem = deletedItem;
	}

	@Override
	public void execute() {
		super.todos.delete(this.deletedItem);
	}

	@Override
	public void inverse() {
		super.todos.add(this.deletedItem);
	}

}
