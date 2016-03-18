package controller;

import model.TodoFile;

public class DisplayOperation extends Operation {
	public DisplayOperation(TodoFile t) {
		super(t);
	}

	@Override
	public void execute() {
		this.todos.display();
	}

	@Override
	public void inverse() {
	}

}
