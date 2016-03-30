package model;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import model.TodoItem.Frequency;

public class TodoFileTests {
	TodoFile todos;

	@Before
	public void setUp() {
		todos = new TodoFile("TodoFileTests.txt");
	}

	@Test
	public void TestAdd() {
		todos.add("The Time is now");
		assert (todos.getItem(0).getContents().equals("The Time is now"));
		todos.add("Potato Meatloaf with Grandma");
		assert (todos.getItem(1).getContents().equals("Potato Meatloaf with Grandma"));
		todos.display();
		assert (todos.getItem(0).getDueDate().equals(null));
		assert (todos.getItem(0).getPriority() == -1);
		assert (todos.getItem(0).getFreq().equals(Frequency.NONE));
		todos.add(3, new Date(), new Date(), "Another test");
		todos.display();
	}

	@Test
	public void TestExit() {
		todos.add("something");
		todos.add("something else");
		todos.add("something else entirely");
		todos.display();
		todos.exit();
		todos = new TodoFile("TodoFileTests.txt");
		todos.display();
		assert (!todos.isEmpty());
	}

}
