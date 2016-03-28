package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DeleteOperationTest extends OperationTest {
	DeleteOperation op;

	@Before
	public void setUp() {
		super.setUp();
		op = new DeleteOperation(this.todos, this.item);
	}
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
