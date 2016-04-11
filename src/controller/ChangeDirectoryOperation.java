package controller;

import model.TodoFile;

/**
 * @author A0149108E
 */
public class ChangeDirectoryOperation extends Operation {
	private CommandInterpreter controller;
	private TodoFile newTodos;

	public ChangeDirectoryOperation(TodoFile t, String newDir, CommandInterpreter controller) {
		super(t);
		this.controller = controller;
		this.newTodos = new TodoFile(newDir);
	}

	@Override
	public void execute() {
		todos.exit();
		controller.setTodoFile(newTodos);
	}

	@Override
	public void inverse() {
		newTodos.exit();
		controller.setTodoFile(todos);
	}

}
