/**
 * 
 */
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
 * @author gabe
 *
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
	 * Test method for {@link view.FlexiArea#nextTimeChunk()}.
	 */
	@Test
	public void testNextTimeChunk() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link view.FlexiArea#previousTimeChunk()}.
	 */
	@Test
	public void testPreviousTimeChunk() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link view.FlexiArea#setTimeState(view.FlexiArea.TimeState)}.
	 */
	@Test
	public void testSetTimeState() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link view.FlexiArea#refresh()}.
	 */
	@Test
	public void testRefresh() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link view.FlexiArea#setMode(view.FlexiArea.Mode)}.
	 */
	@Test
	public void testSetMode() {
		flexiArea.setTimeState(FlexiArea.TimeState.ALL);
		int start = flexiArea.getChildren().size();
		todoFile.add(1, "Hi there");
		flexiArea.setMode(FlexiArea.Mode.SORT_START_DATE);
		assertEquals(flexiArea.getMode(), FlexiArea.Mode.SORT_START_DATE);
		assertEquals(flexiArea.getChildren().size(), start + 1);
	}

	/**
	 * Test method for {@link view.FlexiArea#printStartTodos(java.util.List)}.
	 */
	@Test
	public void testPrintStartTodos() {
		List<GuiTodo> starts = new ArrayList<GuiTodo>();
		starts.add(new GuiTodo(new TodoItem("Hi"), GuiTodo.SoD.START));
		starts.add(new GuiTodo(new TodoItem("There"), GuiTodo.SoD.START));
		starts.add(new GuiTodo(new TodoItem("All"), GuiTodo.SoD.START));
		int s = flexiArea.getChildren().size();
		flexiArea.printGuiTodos(starts);
		assertEquals(s + 3, flexiArea.getChildren().size());
		assertTrue(flexiArea.getChildren().get(s + 2) instanceof Text);
	}

	/**
	 * Test method for {@link view.FlexiArea#printDueTodos(java.util.List)}.
	 */
	@Test
	public void testPrintDueTodos() {
		List<GuiTodo> starts = new ArrayList<GuiTodo>();
		starts.add(new GuiTodo(new TodoItem("Hi"), GuiTodo.SoD.DUE));
		starts.add(new GuiTodo(new TodoItem("There"), GuiTodo.SoD.DUE));
		starts.add(new GuiTodo(new TodoItem("All"), GuiTodo.SoD.DUE));
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
	 * Test method for {@link view.FlexiArea#getMode()}.
	 */
	@Test
	public void testGetMode() {
		assertEquals(flexiArea.getMode(), FlexiArea.DEFAULT_MODE);
	}

	/**
	 * Test method for {@link view.FlexiArea#getNthTodo(int)}.
	 */
	@Test
	public void testGetNthTodo() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link view.FlexiArea#start()}.
	 */
	@Test
	public void testStart() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link view.FlexiArea#end()}.
	 */
	@Test
	public void testEnd() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link view.FlexiArea#getTimeState()}.
	 */
	@Test
	public void testGetTimeState() {
		fail("Not yet implemented");
	}
}
