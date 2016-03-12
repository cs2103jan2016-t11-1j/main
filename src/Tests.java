import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import javafx.application.Application;

public class Tests {
    protected static CommandInterpreter cmdInt = null;
    protected static TodoFile todos = null;


    private void execString (String s) {
        cmdInt.setLastCommand(s);
        cmdInt.executeCommand();
    }
    @Before
    public void setUp() {
        todos = new TodoFile("test.txt");
        cmdInt = new CommandInterpreter(todos);
    }
    @After
    public void tearDown() {
        todos.clear();
        todos.exit();
    }

    @Test
    public void testAdd() {
        execString("add arst");
        execString("add tsra");
        execString("add neio");
        execString("display");

        assertEquals(cmdInt.getEntry(0).getContents(), "arst");
        assertEquals(cmdInt.getEntry(1).getContents(), "tsra");
        assertEquals(cmdInt.getEntry(2).getContents(), "neio");
    }

    @Test
    public void testDelete() {
        execString("add arst");
        execString("add tsra");
        execString("add neio");
        execString("display");
        assertEquals(cmdInt.getEntry(0).getContents(), "arst");
        execString("delete 1");
        assertEquals(cmdInt.getEntry(0).getContents(), "tsra");
        assertEquals(cmdInt.getEntry(1).getContents(), "neio");
        execString("delete 2");
        assertEquals(cmdInt.getEntry(0).getContents(), "tsra");
        execString("delete 1");
        assertEquals(todos.isEmpty(), true);
        execString("display");
    }

    @Test
    public void testClear() {
        execString("add arst");
        execString("add tsra");
        execString("add neio");
        assertEquals(cmdInt.getEntry(0).getContents(), "arst");
        assertEquals(cmdInt.getEntry(1).getContents(), "tsra");
        assertEquals(cmdInt.getEntry(2).getContents(), "neio");
        execString("clear");
        assertEquals(todos.isEmpty(), true);
    }

    @Test
    public void testSort() {
        execString("add z");
        execString("add a");
        execString("add z");
        execString("add y");
        execString("add b");
        execString("sort");
        assertEquals(cmdInt.getEntry(0).getContents(), "a");
        assertEquals(cmdInt.getEntry(1).getContents(), "b");
        assertEquals(cmdInt.getEntry(2).getContents(), "y");
        assertEquals(cmdInt.getEntry(3).getContents(), "z");
        assertEquals(cmdInt.getEntry(4).getContents(), "z");
    }

}
