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
	private static final boolean APPEND_TO_FILE = false;

	public static void main(String[] args) throws IOException {
		String fileName = args[0];
		processInput(fileName);
	}

	public static void processInput(String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName, APPEND_TO_FILE);
			PrintWriter printer = new PrintWriter(writer);

			System.out.printf("Welcome to TextBuddy. %s is ready to use\ncommand: ", fileName);
			CommandInterpreter commander = new CommandInterpreter(printer, fileName);
			while (true) {
				commander.nextCommand();
				commander.executeCommand();
				System.out.print("command: ");
			}
		} catch (IOException e) {
			System.err.printf("Could not open file %s", fileName);
			System.exit(1);
		}
	}
}
