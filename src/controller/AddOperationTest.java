package controller;
/**
 * @author A0149108E
 */
import org.junit.Before;
import org.junit.Test;


public class AddOperationTest extends OperationTest {
	AddOperation op;

	@Before
	public void setUp() {
		super.setUp();
		op = new AddOperation(this.todos, this.item);
	}

	@Test
	@Override
	public void testExecute() {
		op.execute();
		hasAsFirstElement();
	}

	@Test
	@Override
	public void testInverse() {
		op.execute();
		hasAsFirstElement();
		op.inverse();
		isEmptyTodos();
	}

}
