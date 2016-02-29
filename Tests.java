import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
public class Tests {
    protected static CommandInterpreter cmdInt = null;

    private void execString (String s) {
        cmdInt.setLastCommand(s);
        cmdInt.executeCommand();
    }
    @Before
    public void setUp() {
        cmdInt = new CommandInterpreter("test.txt");
    }
    @After
    public void tearDown() {
        cmdInt.exit();
    }

    @Test
    public void testAdd() {
        execString("add arst");
        execString("add tsra");
        execString("add neio");
        execString("display");

        assertEquals(cmdInt.getEntry(0), "arst");
        assertEquals(cmdInt.getEntry(1), "tsra");
        assertEquals(cmdInt.getEntry(2), "neio");
    }

    @Test
    public void testDelete() {
        execString("add arst");
        execString("add tsra");
        execString("add neio");
        execString("display");
        assertEquals(cmdInt.getEntry(0), "arst");
        execString("delete 1");
        assertEquals(cmdInt.getEntry(0), "tsra");
        assertEquals(cmdInt.getEntry(1), "neio");
        execString("delete 2");
        assertEquals(cmdInt.getEntry(0), "tsra");
        execString("delete 1");
        assertEquals(cmdInt.empty(), true);
        execString("display");
    }

    @Test
    public void testClear() {
        execString("add arst");
        execString("add tsra");
        execString("add neio");
        assertEquals(cmdInt.getEntry(0), "arst");
        assertEquals(cmdInt.getEntry(1), "tsra");
        assertEquals(cmdInt.getEntry(2), "neio");
        execString("clear");
        assertEquals(cmdInt.empty(), true);
    }

    @Test
    public void testSort() {
        execString("add z");
        execString("add a");
        execString("add z");
        execString("add y");
        execString("add b");
        execString("sort");
        assertEquals(cmdInt.getEntry(0), "a");
        assertEquals(cmdInt.getEntry(1), "b");
        assertEquals(cmdInt.getEntry(2), "y");
        assertEquals(cmdInt.getEntry(3), "z");
        assertEquals(cmdInt.getEntry(4), "z");
    }

}
