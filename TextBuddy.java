import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * Assume: that you want to clobber already created files. If not, APPENDTOFILE = true.
 * Assume: we're using option (c) for when to write to files, i.e. when the user exits the program.
 * Assume: a "write" method is also provided to force a write before exit.
 * Assume: calling 'delete' with no number arguments deletes the first element.
 * Assume: the delete method checks for problems and prints the problems to stderr
 *
 */
public class TextBuddy {
    private static final String prompt = "command: ";

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Must supply a file name");
            System.exit(1);
        }
        String fileName = args[0];
        processInput(fileName);
    }

    public static void processInput(String fileName) {
        CommandInterpreter commander = new CommandInterpreter(fileName);
        System.out.printf("Welcome to TextBuddy. %s is ready to use\ncommand: ", fileName);
        while (true) {
            commander.nextCommand();
            commander.executeCommand();
            System.out.print(prompt);
        }
    }
}
