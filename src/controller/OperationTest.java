package controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;

import model.TodoFile;
import model.TodoItem;

public abstract class OperationTest {
    public static final String TESTNAME = "tester.txt";
    TodoFile todos;
    TodoItem item;
	
    @Before
    public void setUp() {
        todos = new TodoFile(TESTNAME);
        item = new TodoItem(TodoItem.Status.TODO, "This is a test add.");
    }
    protected void hasAsFirstElement() {
        assertEquals(this.todos.getItem(0), this.item);
    }
    protected void isEmptyTodos() {
        assertEquals(this.todos.isEmpty(), true);
    }
    public abstract void testExecute();
    public abstract void testInverse();
}
