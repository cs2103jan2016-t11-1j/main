package controller;



import java.util.Date;

import model.TodoFile;
import model.TodoItem;

public class UpdateDueDateOperation extends Operation {
	private TodoItem itemToUpdate;
	private Date newDate;
	private Date oldDate;

	public UpdateDueDateOperation(TodoFile t, TodoItem itemToUpdate, String date) {
		super(t);
		this.itemToUpdate = itemToUpdate;
		this.oldDate = itemToUpdate.getDueDate();
		DateParser natty = new DateParser();
		this.newDate = natty.parse(date);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		super.todos.updateDueDate(itemToUpdate, newDate);
	}

	@Override
	public void inverse() {
		// TODO Auto-generated method stub
		super.todos.updateDueDate(itemToUpdate, oldDate);
	}

}
