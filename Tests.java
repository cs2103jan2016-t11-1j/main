import static org.junit.Assert.assertEquals;
import java.io.*;
import org.junit.Test;

public class Tests {
    public static void changeStdIn(String cmd) {
        ByteArrayInputStream in = new ByteArrayInputStream(cmd.getBytes());
        System.setIn(in);
    }

    public static String getStdOut() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        return baos.toString();
    }
    public static void inputStateTest(String in, String out, CommandInterpreter commander, PrintWriter printer) {
        changeStdIn(in + "\ndisplay\n");

        commander.nextCommand();
        assertEquals(in, commander.getLastCommand());

        commander.executeCommand();
        assertEquals(in, commander.getLastCommand());
        assertEquals(out, getStdOut());
    }

    @Test
    public static void testCommandInterpreter() {
        PrintWriter printer = null;
        try {
            FileWriter writer = new FileWriter("text.txt", false);
            printer = new PrintWriter(writer);
        } catch (IOException e) {
            System.err.println("file failed to open.");
            System.exit(1);
        }
        CommandInterpreter commander = new CommandInterpreter(printer, "test.txt");

        String TEST1 = "add what's up dawg?";
        String STATE1 = "\n1. what's up dawg?\n";
        inputStateTest(TEST1, STATE1, commander, printer);
        String TEST2 = "add what's up dood?";
        String STATE2 = "\n1. what's up dawg?\n2. what's up dood?";
        inputStateTest(TEST2, STATE2, commander, printer);
        String TEST3 = "delete 1";
        String STATE3 = "\n1. what's up dood?\n";
        inputStateTest(TEST3, STATE3, commander, printer);
        String TEST4 = "delete 1";
        String STATE4 = "The file is empty";
        inputStateTest(TEST4, STATE4, commander, printer);

        String TEST5 = "add what's up dawg?";
        String STATE5 = "\n1. what's up dawg?\n";
        inputStateTest(TEST5, STATE5, commander, printer);
        String TEST6 = "add what's up dood?";
        String STATE6 = "\n1. what's up dawg?\n2. what's up dood?";
        inputStateTest(TEST6, STATE6, commander, printer);
        String TEST7 = "clear";
        String STATE7 = "The file is empty";
        inputStateTest(TEST7, STATE7, commander, printer);
    }
    public static void main (String[] args) {
        testCommandInterpreter();
    }
}
