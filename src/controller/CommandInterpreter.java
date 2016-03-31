package controller;

import java.util.Date;
import model.TodoFile;
import model.TodoItem;
import model.TodoItem.Frequency;
import view.FlexiArea;

public class CommandInterpreter {
	private static final String WHITESPACE = "\\s+"; // whitespace regex
	private static final char SEPERATOR = '#';
	private static String toDoMessage = "";
	public static Integer intPriority;
	private static String dateForNatty = "";
	private static Date parsedDate;
	private String lastCommand;
	private TodoFile todos;
	private FlexiArea flexiView;
	private Undoer undos;

	public CommandInterpreter(TodoFile todos, FlexiArea flexiView) {
		this.todos = todos;
		this.lastCommand = null;
		this.flexiView = flexiView;
		undos = new Undoer();
		flexiView.setMode(FlexiArea.Mode.SORT_CONTENTS);
	}

	public void nextCommand(String text) {
		lastCommand = text;
	}

	public void executeCommand() {
		/*
		 * String is split into command, message, priority and date and stored
		 * in splitString[]
		 *
		 */
		Operation op = null;
		String[] splitString = lastCommand.split(WHITESPACE);
		String command = splitString[0];

		// TODO make the command ignore case
		switch (command) {

		case "display":
		case "dis":
		case "disp":
		case "dsp":
			op = new DisplayOperation(todos);
			op.execute();
			break;

		case "add":
		case "ad":
		case "addd":
		case "asd":
		case "ads":
			// TODO parser still doesn't switch properly.
			int hexIndex = -1;
			for (int j = 1; j < splitString.length; j++) {
				if (splitString[j].charAt(0) == SEPERATOR) {
					hexIndex = j;
					break;
				} else {
					toDoMessage = toDoMessage + " " + splitString[j];
				}
			}
			toDoMessage = toDoMessage.trim();

			String hexedPriority = splitString[hexIndex];
			if (!hexedPriority.substring(1).equals("")) {
				intPriority = Integer.parseInt(hexedPriority.substring(1));
			} else {
				intPriority = -1;
			}
			/*
			 * for (int j = 1; j < hexIndex; j++){ toDoMessage = toDoMessage +
			 * " " + splitString[j]; } toDoMessage = toDoMessage.trim();
			 */
			for (int j = hexIndex + 1; j < splitString.length; j++) {
				dateForNatty = dateForNatty + " " + splitString[j];
			}
			dateForNatty = dateForNatty.trim();

			DateParser dp = new DateParser();
			parsedDate = dp.parse(dateForNatty);

			dateForNatty = "";

			op = new AddOperation(todos,
					new TodoItem(TodoItem.Status.TODO, intPriority, null, parsedDate, toDoMessage, Frequency.NONE));
			op.execute();
			toDoMessage = "";
			break;
		case "delete":
		case "del":
		case "de":
		case "dlt":
		case "delt":
		case "delet":
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
			op = new DeleteOperation(todos, todos.getItem(index-1));
			op.execute();
			break;
		case "clear":
		case "clr":
		case "cl":
		case "cle":
		// TODO Change to OP
			todos.clear();
			System.out.println("all todos deleted");
			break;
		case "exit":
		case "ext":
		case "ex":
			exit();
			break;
		case "write":
		case "wrt":
		case "writ":
		case "rit":
			// TODO Change to OP
			todos.write();
			break;
		case "sort":
		case "srt":
		case "sor":
			todos.sortByContents();
			System.out.println("sorted by Contents");
			break;
		case "search":
		case "seach":
		case "sch":
		case "srch":
		case "serch":
		case "src":
			//
			String rest = lastCommand.substring(command.length()).replaceAll(WHITESPACE, "");
			todos.searchString(rest);
			break;
		case "whatmode":
			switch (flexiView.getMode()) {
			case HEAT_MAP:
				System.out.println("The current mode is Heat map.");
				break;
			case SORT_CONTENTS:
				System.out.println("The current mode is Sort Contents.");
				break;
			case SORT_DATE:
				System.out.println("The current mode is Sort Date.");
				break;
			case SORT_PRIORITY:
				System.out.println("The current mode is Sort Priority.");
				break;
			case SORT_STATUS:
				System.out.println("The current mode is Sort Status.");
				break;
			default:
				break;
			}
			break;
		case "mode":
		case "mod":
		case "moe":
		case "modd":
			String newMode = lastCommand.substring(command.length()).trim();
			switch (newMode.toLowerCase()) {
			case "help":
				System.out.println("The possible modes are date, priority, status, contents, and heat.");
				break;
			case "date":
				flexiView.setMode(FlexiArea.Mode.SORT_DATE);
				break;
			case "priority":
				flexiView.setMode(FlexiArea.Mode.SORT_PRIORITY);
				break;
			case "status":
				flexiView.setMode(FlexiArea.Mode.SORT_STATUS);
				break;
			case "contents":
				flexiView.setMode(FlexiArea.Mode.SORT_CONTENTS);
				break;
			case "heat":
				flexiView.setMode(FlexiArea.Mode.HEAT_MAP);
				break;
			default:
				System.out.println("Mode not recognized.");
				break;
			}
			break;
		case "whattime":
			switch (flexiView.getTimeState()) {
			case ALL:
				System.out.println("The current time mode is all from " + flexiView.start() + " to " + flexiView.end());
				break;
			case DAY:
				System.out.println("The current time mode is day from " + flexiView.start() + " to " + flexiView.end());
				break;
			case FUTURE:
				System.out.println(
						"The current time mode is future from " + flexiView.start() + " to " + flexiView.end());
				break;
			case MONTH:
				System.out
						.println("The current time mode is month from " + flexiView.start() + " to " + flexiView.end());
				break;
			case WEEK:
				System.out
						.println("The current time mode is week from " + flexiView.start() + " to " + flexiView.end());
				break;
			}
			break;
		case "time":
		case "tim":
		case "tme":
		case "tie":
			String newTime = lastCommand.substring(command.length()).trim();
			switch (newTime.toLowerCase()) {
			case "help":
				System.out.println("Possible intervals are day, week, month, all, future");
				break;
			case "day":
				flexiView.setTimeState(FlexiArea.TimeState.DAY);
				System.out.println("Set time interval to day");
				break;
			case "week":
				flexiView.setTimeState(FlexiArea.TimeState.WEEK);
				System.out.println("Set time interval to week");
				break;
			case "month":
				flexiView.setTimeState(FlexiArea.TimeState.MONTH);
				System.out.println("Set time interval to month");
				break;
			case "all":
				flexiView.setTimeState(FlexiArea.TimeState.ALL);
				System.out.println("Set time interval to all");
				break;
			case "future":
				flexiView.setTimeState(FlexiArea.TimeState.FUTURE);
				System.out.println("Set time interval to future");
				break;
			default:
				System.out.println("Time chunk not recognized not recognized.");
				break;
			}
			break;
		case "todo":
		case "done":
			rest = lastCommand.substring(command.length()).replaceAll(WHITESPACE, "");
			try {
				todos.toggle(todos.getItem((Integer.parseInt(rest))-1));
			} catch (NumberFormatException e) {
				System.out.println("Bad int format");
			}
			break;
		case "previous":
		case "prev":
			flexiView.previousTimeChunk();
			break;
		case "next":
		case "nxt":
			flexiView.nextTimeChunk();
			break;
		default:
			System.out.println("Command not recognized.");
			break;
		}
		undos.add(op);
		flexiView.setMode(FlexiArea.Mode.SORT_CONTENTS);
	}

	public String getLastCommand() {
		return lastCommand;
	}

	public void exit() {
		todos.exit();
		System.exit(0);
	}

	public void undo() {
		undos.undo();
	}
}
