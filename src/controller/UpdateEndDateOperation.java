
package controller;

import java.util.Date;

import model.TodoFile;
import model.TodoItem;

/**
 * @@author A0130157R
 */
public class UpdateEndDateOperation extends Operation {
	private TodoItem itemToUpdate;
	private Date newDate;
	private Date oldDate;

	public UpdateEndDateOperation(TodoFile t, TodoItem itemToUpdate, String date) {
		super(t);
		this.itemToUpdate = itemToUpdate;
		this.oldDate = itemToUpdate.getDueDate();
		DateParser natty = new DateParser();
		this.newDate = natty.parse(date);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		super.todos.updateEndDate(itemToUpdate, newDate);
	}

	@Override
	public void inverse() {
		// TODO Auto-generated method stub
		super.todos.updateEndDate(itemToUpdate, oldDate);
	}

}
