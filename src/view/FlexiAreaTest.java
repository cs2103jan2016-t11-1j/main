package view;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.text.Text;
import model.TodoFile;
import model.TodoItem;

/**
 * @@author A0149108E
 */
public class FlexiAreaTest {
	private FlexiArea flexiArea;
	private TodoFile todoFile;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		todoFile = new TodoFile("test.arst");
		flexiArea = new FlexiArea(todoFile);
	}

	/**
	 * Test method for {@link view.FlexiArea#FlexiArea()}.
	 */
	@Test
	public void testFlexiArea() {
		assertEquals(flexiArea.getTimeState(), FlexiArea.DEFAULT_TIME);
		assertEquals(flexiArea.getMode(), FlexiArea.DEFAULT_MODE);
	}

	/**
	 * Test method for {@link view.FlexiArea#setMode(view.FlexiArea.Mode)}.
	 */
	@Test
	public void testSetMode() {
		flexiArea.setTimeState(FlexiArea.TimeState.FLOATING);
		int start = flexiArea.getChildren().size();
		todoFile.add(1, "Hi there");
		flexiArea.setMode(FlexiArea.Mode.SORT_START_DATE);
		assertEquals(flexiArea.getMode(), FlexiArea.Mode.SORT_START_DATE);
		assertEquals(flexiArea.getChildren().size(), start + 1);
	}

	public List<GuiTodo> addStarts() {
		List<GuiTodo> starts = new ArrayList<GuiTodo>();
		starts.add(new GuiTodo(new TodoItem("Hi"), GuiTodo.SoD.START));
		starts.add(new GuiTodo(new TodoItem("There"), GuiTodo.SoD.START));
		starts.add(new GuiTodo(new TodoItem("All"), GuiTodo.SoD.START));
		return starts;
	}

	public List<GuiTodo> addDues() {
		List<GuiTodo> starts = new ArrayList<GuiTodo>();
		starts.add(new GuiTodo(new TodoItem("Hi"), GuiTodo.SoD.DUE));
		starts.add(new GuiTodo(new TodoItem("There"), GuiTodo.SoD.DUE));
		starts.add(new GuiTodo(new TodoItem("All"), GuiTodo.SoD.DUE));
		return starts;
	}

	/**
	 * Test method for {@link view.FlexiArea#printStartTodos(java.util.List)}.
	 */
	@Test
	public void testPrintStartTodos() {
		int s = flexiArea.getChildren().size();
		List<GuiTodo> starts = addStarts();
		flexiArea.printGuiTodos(starts);
		assertEquals(s + 3, flexiArea.getChildren().size());
		assertTrue(flexiArea.getChildren().get(s + 2) instanceof Text);
	}

	/**
	 * Test method for {@link view.FlexiArea#printDueTodos(java.util.List)}.
	 */
	@Test
	public void testPrintDueTodos() {
		List<GuiTodo> starts = addDues();
		int s = flexiArea.getChildren().size();
		flexiArea.printGuiTodos(starts);
		assertEquals(s + 3, flexiArea.getChildren().size());
		assertTrue(flexiArea.getChildren().get(s + 2) instanceof Text);
	}

	/**
	 * Test method for {@link view.FlexiArea#println(java.lang.String)}.
	 */
	@Test
	public void testPrintln() {
		int s = flexiArea.getChildren().size();
		flexiArea.println("Hi There");
		assertEquals(s + 1, flexiArea.getChildren().size());
		assertTrue(flexiArea.getChildren().get(s) instanceof Text);
		assertTrue(((Text) flexiArea.getChildren().get(s)).getText().equals("Hi There"));
	}

	/**
	 * Test method for {@link view.FlexiArea#printGuiTodos(java.util.List)}.
	 */
	@Test
	public void testPrintGuiTodos() {
		List<GuiTodo> starts = addStarts();
		starts.addAll(addDues());
		int s = flexiArea.getChildren().size();
		flexiArea.printGuiTodos(starts);
		assertEquals(s + 6, flexiArea.getChildren().size());
		assertTrue(flexiArea.getChildren().get(s + 2) instanceof Text);
	}

	/**
	 * Test method for {@link view.FlexiArea#getMode()}.
	 */
	@Test
	public void testGetMode() {
		assertEquals(flexiArea.getMode(), FlexiArea.DEFAULT_MODE);
	}

	/**
	 * Test method for {@link view.FlexiArea#getTimeState()}.
	 */
	@Test
	public void testGetTimeState() {
		assertEquals(flexiArea.getTimeState(), FlexiArea.DEFAULT_TIME);
	}

	/**
	 * Test method for {@link view.FlexiArea#setTodos(model.TodoFile)}.
	 */
	@Test
	public void testSetTodos() {
		List<GuiTodo> start = addStarts();
		flexiArea.printGuiTodos(start);
		int s = flexiArea.getChildren().size();
		flexiArea.setTodos(new TodoFile("arst.todo"));
		assertTrue(flexiArea.getChildren().size() != s);
	}

	/**
	 * Test method for {@link view.FlexiArea#toggleDoneLast()}.
	 */
	@Test
	public void testToggleDoneLast() {
		boolean prev = flexiArea.isDoneLast();
		flexiArea.toggleDoneLast();
		assertTrue(prev != flexiArea.isDoneLast());
	}
}
