
package controller;

import model.TodoFile;
import model.TodoItem;
/**
 * @@author A0130157R
 */
public class UpdatePriorityOperation extends Operation {
	private TodoItem itemToUpdate;
	private Integer newPriority;
	private Integer oldPriority;

	public UpdatePriorityOperation(TodoFile t, TodoItem itemToUpdate, Integer p) {
		super(t);
		this.itemToUpdate = itemToUpdate;
		newPriority = p;
		oldPriority = itemToUpdate.getPriority();
	}

	@Override
	public void execute() {
		super.todos.updatePriority(itemToUpdate, newPriority);
	}

	@Override
	public void inverse() {
		super.todos.updatePriority(itemToUpdate, oldPriority);
	}

}
