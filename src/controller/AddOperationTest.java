package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.TodoFile;
import model.TodoItem;

public class AddOperationTest extends OperationTest {
	AddOperation op;

	@Before
	public void setUp() {
		super.setUp();
		op = new AddOperation(this.todos, this.item);
	}
	@Test
	public void testAdd() {
		fail("Not yet implemented");
	}

}
