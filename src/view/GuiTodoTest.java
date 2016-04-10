package view;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.TodoItem;

public class GuiTodoTest {
	List<GuiTodo> todos;
	
	@Before
	public void setUp() throws Exception {
		todos = new ArrayList<GuiTodo>();
		assert false;
	}

	public void addStandard() {
		todos.add(new GuiTodo(new TodoItem("hi"), GuiTodo.SoD.FLOATING));
		todos.add(new GuiTodo(new TodoItem(TodoItem.Status.DONE, -1,
				new Date(), null, "there", TodoItem.Frequency.NONE), GuiTodo.SoD.START));
		todos.add(new GuiTodo(new TodoItem(TodoItem.Status.TODO, -1,
				null, new Date(), "man", TodoItem.Frequency.NONE), GuiTodo.SoD.DUE));
	}

	@Test
	public void testNormal() {
		addStandard();
	}

	@Test
	public void testGetStatus() {
		addStandard();
		assertEquals(todos.get(0).getStatus(), GuiTodo.SoD.FLOATING);
		assertEquals(todos.get(1).getStatus(), GuiTodo.SoD.START);
		assertEquals(todos.get(2).getStatus(), GuiTodo.SoD.DUE);
	}

}
