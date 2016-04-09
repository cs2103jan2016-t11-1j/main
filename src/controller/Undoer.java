package controller;

/**
 * implements a fixed size stack that holds the operations and their inverses.
 * 
 * @author gabe
 *
 */
public class Undoer {
	public static final int MAX_UNDO = 50;
	private FixedSizeStack<Operation> operations;

	public Undoer() {
		operations = new FixedSizeStack<Operation>(MAX_UNDO);
	}

	public void add(Operation op) {
		assert op != null;
		operations.push(op);
	}

	public void undo() {
		if (operations.size() > 0)
			operations.pop().inverse();
	}
}
