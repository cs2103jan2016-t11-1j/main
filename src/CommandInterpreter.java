import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;



public class CommandInterpreter {
	private static final String WHITESPACE = "\\s+"; // whitespace regex
	private static final char SEPERATOR = '#'; 
	private static String toDoMessage = "";
	public static Integer intPriority;
	private static String dateForNatty = "";
	private static Date parsedDate;
	private String lastCommand;
	private BufferedReader in;
	private TodoFile todos;

	public CommandInterpreter(TodoFile todos) {
		this.todos = todos;
		lastCommand = null;
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	public void nextCommand() {
		try {
			lastCommand = in.readLine();
		} catch (IOException io) {
			io.printStackTrace();
			todos.exit();
			System.exit(0);
		}
	}

	public void executeCommand() {
		//
		// TODO: transfer most of this stuff to the TodoFile class
		//
		/* 
		 * String is split into command, message, 
		 * priority and date and stored in splitString[]
		 *
		 */

		//
		String[] splitString = lastCommand.split(WHITESPACE);
		String command = splitString[0];

		int hexIndex = -1;
		for (int j = 1; j < splitString.length ; j++) {
			if (splitString[j].charAt(0) == SEPERATOR) {  
				hexIndex = j;
				break;
			}
		}

		String hexedPriority = splitString[hexIndex];
		if (!hexedPriority.substring(1).equals("")){
			intPriority = Integer.parseInt(hexedPriority.substring(1));
		}else{
			intPriority = -1;
		}

		for (int j = 1; j < hexIndex; j++){
			toDoMessage = toDoMessage + " " + splitString[j];
		}
		toDoMessage = toDoMessage.trim();

		for (int j = hexIndex+1; j < splitString.length; j++){
			dateForNatty = dateForNatty + " " + splitString [j];
		}
		dateForNatty = dateForNatty.trim();

		DateParser dp = new DateParser();
		parsedDate = dp.parse(dateForNatty);

		switch (command) {
		case "display":
			todos.display();
			break;
		case "add":
			//assuming the whitespace between the command and what is to be added is not significant
			//
			String rest = lastCommand.substring(command.length()).replaceAll("^\\s+", "");
			todos.add(intPriority, parsedDate, toDoMessage);
			break;
		case "delete":
			if (todos.isEmpty()) {
				System.out.printf("No todos\n");
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
			todos.delete(index);
			break;
		case "clear":
			todos.clear();
			System.out.println("all todos deleted");
			break;
		case "exit":
			exit();
			System.exit(0);
			break;
		case "write":
			todos.write();
			break;
		case "sort":
			todos.sortByContents();
			todos.printFile();
			break;
			case "search":
			//
			rest = lastCommand.substring(command.length()).replaceAll("^\\s+", "");
			todos.searchString(rest);
			break;
		default:
			System.out.print("Command not recognized.\n");
			break;
		}

	}

	public String getLastCommand() {
		return lastCommand;
	}
	protected void setLastCommand(String last) {
		this.lastCommand = last;
	}
	protected TodoItem getEntry(int i) {
		return todos.getItem(i);
	}
	private void exit() {
		todos.exit();
		System.exit(0);
	}
	/*
	public Integer getStuff() {
		return intPriority;
	}
	 */
}
