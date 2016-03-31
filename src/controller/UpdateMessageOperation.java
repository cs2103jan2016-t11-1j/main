package controller;

import model.TodoFile;
import model.TodoItem;

public class UpdateMessageOperation extends Operation {
	private TodoItem itemToUpdate;
	private String newMessage;
	private String oldMessage;

	public UpdateMessageOperation(TodoFile t, TodoItem itemToUpdate, String message) {
		super(t);
		this.itemToUpdate = itemToUpdate;
		newMessage = message;
		oldMessage = itemToUpdate.getContents();
	}

	@Override
	public void execute() {
		super.todos.updateContents(itemToUpdate, newMessage);
	}

	@Override
	public void inverse() {
		super.todos.updateContents(itemToUpdate, oldMessage);
	}

}
