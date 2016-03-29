package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import model.TodoFile;
import model.TodoItem;
import view.FlexiArea;

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
  private FlexiArea flexiView;
  private Undoer undos;

  public CommandInterpreter(TodoFile todos, FlexiArea flexiView) {
      this.todos = todos;
      this.lastCommand = null;
      this.flexiView = flexiView;
      undos = new Undoer();
      flexiView.setState(FlexiArea.FlexiState.SORT_CONTENTS);
  }

  public void nextCommand(String text) {
      lastCommand = text;
  }

	public void executeCommand() {
		/*
		 * String is split into command, message,
		 * priority and date and stored in splitString[]
		 *
		 */
    Operation op = null;
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
      op = new DisplayOperation(todos);
      op.execute();
			break;
		case "add":
			//assuming the whitespace between the command and what is to be added is not significant
			//
      op = new AddOperation(todos, new TodoItem(TodoItem.Status.TODO, intPriority, parsedDate, toDoMessage));
      op.execute();
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
      op = new DeleteOperation(todos, todos.getItem(index));
      op.execute();
			break;
		case "clear":
        //TODO Change to OP
			todos.clear();
			System.out.println("all todos deleted");
			break;
		case "exit":
			exit();
			break;
		case "write":
        //TODO Change to OP
			todos.write();
			break;
		case "sort":
      todos.sortByContents();
      System.out.println("sorted by Contents");
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
    undos.add(op);
    flexiView.setState(FlexiArea.FlexiState.SORT_CONTENTS);
	}

	public String getLastCommand() {
		return lastCommand;
	}
	private void exit() {
		todos.exit();
		System.exit(0);
	}
  public void undo() {
      undos.undo();
  }
	/*
	public Integer getStuff() {
		return intPriority;
	}
	 */
}
