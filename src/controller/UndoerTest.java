package controller;
/**
 * @@author A0149108E
 */
import org.junit.Before;
import org.junit.Test;

public class UndoerTest extends OperationTest {
	Undoer u;

	@Before
	public void setUp() {
		super.setUp();
		u = new Undoer();
	}

	@Test
	public void testUndoAdd() {
		AddOperation op = new AddOperation(this.todos, this.item);
		op.execute();
		u.add(op);
		hasAsFirstElement();
		u.undo();
		isEmptyTodos();
	}

	@Test
	public void testUndoDelete() {
		this.todos.add(this.item);
		DeleteOperation op = new DeleteOperation(this.todos, this.item);
		op.execute();
		u.add(op);
		isEmptyTodos();
		u.undo();
		hasAsFirstElement();
	}

	@Override
	public void testExecute() {
	}

	@Override
	public void testInverse() {
	}

}
