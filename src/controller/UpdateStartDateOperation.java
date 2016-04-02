package controller;



import java.util.Date;

import model.TodoFile;
import model.TodoItem;

public class UpdateStartDateOperation extends Operation {
	private TodoItem itemToUpdate;
	private Date newDate;
	private Date oldDate;

	public UpdateStartDateOperation(TodoFile t, TodoItem itemToUpdate, String date) {
		super(t);
		this.itemToUpdate = itemToUpdate;
		this.oldDate = itemToUpdate.getStartDate();
		DateParser natty = new DateParser();
		this.newDate = natty.parse(date);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		super.todos.updateStartDate(itemToUpdate, newDate);
	}

	@Override
	public void inverse() {
		// TODO Auto-generated method stub
		super.todos.updateStartDate(itemToUpdate, oldDate);
	}

}
