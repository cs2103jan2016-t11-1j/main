package controller;
import model.TodoFile;

public abstract class Operation {
	protected TodoFile todos;
	public Operation(TodoFile t) {
		todos = t;
	}
	public abstract void execute();
	public abstract void inverse();
}
