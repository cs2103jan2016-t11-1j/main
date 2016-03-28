package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.TodoFile;
import model.TodoItem;

public class OperationTest {
	public static final String TESTNAME = "tester.txt";
	TodoFile todos;
	TodoItem item;
	
	@Before
	public void setUp() {
		todos = new TodoFile(TESTNAME);
		item = new TodoItem(TodoItem.Status.TODO, "This is a test add.");
	}
}
