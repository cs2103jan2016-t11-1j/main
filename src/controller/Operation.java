package controller;
import model.TodoFile;
/**
 * The operation class is an abstraction over transactions on the todo file
 * classes need to implement execute and inverse, which should result in the same 
 * todo file.
 * 
 * @param t todofile on which the operations are being performed.
 */
public abstract class Operation {
	protected TodoFile todos;
	public Operation(TodoFile t) {
		todos = t;
	}
	public abstract void execute();
	public abstract void inverse();
}
