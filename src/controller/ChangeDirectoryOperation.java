package controller;

import model.TodoFile;

public class ChangeDirectoryOperation extends Operation {
	private String newDir;
	private CommandInterpreter controller;
	private TodoFile newTodos;

	public ChangeDirectoryOperation(TodoFile t, String newDir, CommandInterpreter controller) {
		super(t);
		this.newDir = newDir;
		this.controller = controller;
		this.newTodos = new TodoFile(newDir);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		todos.exit();
		controller.setTodoFile(newTodos);
	}

	@Override
	public void inverse() {
		newTodos.exit();
		controller.setTodoFile(todos);
	}

}
