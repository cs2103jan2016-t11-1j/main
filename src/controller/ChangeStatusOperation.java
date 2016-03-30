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
		System.out.println("hi");
		this.todos.changeStatus(item);
	}

	@Override
	public void inverse() {
		this.todos.changeStatus(item);
	}

}
