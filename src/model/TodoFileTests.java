package model;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import controller.DateParser;
import model.TodoItem.Frequency;

/**
 * 
 * @author A0135747X
 *
 */
public class TodoFileTests {
	TodoFile todos;

	@Before
	public void setUp() {
		todos = new TodoFile("TodoFileTests.txt");
	}

	@Test
	public void TestAdd() {
		System.out.println("////___NOW TESTING ADD METHOD IN TODOFILE CLASS___\\\\");
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
		todos.clear();
	}

	@Test
	public void TestExit() {
		System.out.println("////___NOW TESTING EXIT METHOD IN TODOFILE CLASS___\\\\");
		todos.add("something");
		todos.add("something else");
		todos.add("something else entirely");
		todos.display();
		todos.exit();
		todos = new TodoFile("TodoFileTests.txt");
		todos.display();
		assert (!todos.isEmpty());
		todos.clear();
	}

	@Test
	public void TestPowerSearch() {
		DateParser dp = new DateParser();
		String now = "now";
		String then = "7 days from now";
		System.out.println("////___NOW TESTING POWER SEARCH METHOD IN TODOFILE CLASS___\\\\");
		todos.add(3, new Date(2000000), new Date(5000000), "SAN CHEZ NO!");
		todos.add(2, new Date(6000000), new Date(8000000), "SAN JOSE YES!");
		todos.add(1, new Date(4500000), new Date(7500000), "SAN RICO YO!");
		todos.add(4, new Date(30000000), new Date(80000000), "I'm here just to test this");
		todos.add(3, new Date(20000000), new Date(70000000), "SANPAI NOTICE ME!");
		todos.add(2, new Date(60000000), new Date(90000000), "YOUR WAIFU IS TRASH!");
		todos.add("What is the topic?");
		todos.powerSearchString("SAN YO");
		todos.powerSearchString("YO SAN");
		todos.findOverlap();
		todos.add(9, new Date(6000000), new Date(7000000), "Another test");
		todos.add(19, new Date(6500000), new Date(7600000), "The test is a lie");
		todos.searchInTimeBlock((dp.parse(now)), (dp.parse(then)));
		todos.clear();
	}
	
	@Test
	public void TestDateSearch() {
		todos.add(3, new Date(200000000), new Date(230000000), "first entry");
		todos.add(2, "MAMA! OOOOoooooOOOOHHHHH", new Date(190000000));
		todos.searchDate(new Date(200000000));
		todos.clear();
	}
	
	@Test
	public void TestRecur() {
		todos.add(3, "Dragon Warrior", new Date(938234));
		todos.display();
		todos.updateRecur();
		todos.display();
	}
}
