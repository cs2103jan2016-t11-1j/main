import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CommandInterpreter {
	private static final String WHITESPACE = "\\s+"; // whitespace regex
	private String lastCommand;
	private BufferedReader in;
	private ArrayList<String> fileState;
	private PrintWriter fileWriter;
	private String fileName;

	public CommandInterpreter(PrintWriter p, String fN) {
		lastCommand = null;
		in = new BufferedReader(new InputStreamReader(System.in));
		fileState = new ArrayList<String>();
		fileWriter = p;
		fileName = fN;
	}

	public void nextCommand() {
		try {
			lastCommand = in.readLine();
		} catch (IOException io) {
			io.printStackTrace();
			exit();
		}
	}

	public void executeCommand() {
		String[] splitString = lastCommand.split(WHITESPACE);

		switch (splitString[0]) {
		case "display":
			if (fileState.size() == 0) {
				System.out.printf("%s is empty\n", fileName);
				return;
			}
			printFile();
			break;
		case "add":
			//assuming the whitespace between the command and what is to be added is not significant
			String rest = lastCommand.substring(splitString[0].length()).replaceAll("^\\s+", "");
			fileState.add(rest);
			System.out.printf("added to %s: \"%s\"\n", fileName, rest);
			break;
		case "delete":
			if (fileState.size() == 0) {
				System.out.printf("%s is empty\n", fileName);
				return;
			}
			int index;
			if (splitString.length < 2) {
				System.out.println("No number supplied, deleting first element.");
				index = 1;
			} else {
				try {
					index = Integer.parseInt(splitString[1]);
				} catch (NumberFormatException e) {
					System.out.print("Parameter must be a number\n");
					return;
				}
			}
			if (index < 1 || index > fileState.size()) {
				System.out.printf("The index must be between 1 and %d, inclusive.\n", fileState.size());
			} else {
				System.out.printf("deleted from %s: \"%s\"\n", fileName, fileState.remove(index - 1));
			}
			break;
		case "clear":
			fileState.clear();
			System.out.printf("all content deleted from %s\n", fileName);
			break;
		case "exit":
			exit();
			break;
		case "write":
			write();
			break;
		default:
			System.out.print("Command not recognized.\n");
			break;
		}

	}
	public void exit() {
		write();
		fileWriter.close();
		System.exit(0); //the program has successfully finished
	}

	private void write() {
		for (int i = fileState.size(); i > 0; i--) {
			fileWriter.println(fileState.remove(0));
		}
		fileWriter.flush();
		System.out.printf("Wrote to %s.\n", fileName);
	}

	private void printFile() {
		int i = 1;
		for (String s : fileState) {
			System.out.printf("%d. %s\n", i, s);
			i++;
		}
	}


	public String getLastCommand() {
		return lastCommand;
	}
}
