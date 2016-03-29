/**
 * 
 */
package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.TodoItem;

/**
 * @author gabe
 *
 */
public class ChangeStatusOperationTest extends OperationTest {
	ChangeStatusOperation op;

	/* (non-Javadoc)
	 * @see controller.OperationTest#setUp()
	 */
	@Before
	public void setUp() {
		super.setUp();
		op = new ChangeStatusOperation(todos, item);
	}

	/**
	 * Test method for {@link controller.ChangeStatusOperation#execute()}.
	 */
	@Test
	@Override
	public void testExecute() {
		op.execute();
		assertTrue(item.getStatus() == TodoItem.Status.DONE);
		op.execute();
		assertTrue(item.getStatus() == TodoItem.Status.TODO);
	}

	/**
	 * Test method for {@link controller.ChangeStatusOperation#inverse()}.
	 */
	@Test
	@Override
	public void testInverse() {
		op.execute();
		assertTrue(item.getStatus() == TodoItem.Status.DONE);
		op.inverse();
		assertTrue(item.getStatus() == TodoItem.Status.TODO);
	}

}
