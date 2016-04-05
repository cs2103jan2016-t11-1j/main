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
	private static String startDateForNatty = "";
	private static String endDateForNatty = "";
	private static Date parsedDueDate;
	private static Date parsedStartDate;
	private static Date parsedEndDate;
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
		 * String is split into command, message, priority and date
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
			 * Date parsing starts here Walkthrough for the code below using
			 * example: 'tomorrow from 5 to 6' 
			 * 1. Search for 'from' and 'to'equivalents. 
			 * 2. If found then: 
			 *   a. startdate = tomorrow + at + 5
			 *   b. enddate = tomorrow + at + 6 
			 *   (Note how 'from' and 'to' is changed to 'at' for natty to recognize) 
			 * 3. If not found then send entire string as duedate (like before). 
			 * Eg. day after tomorrow(no 'from' or 'to' found, so can parse entire string with natty)
			 * 4. For adding, if start and end date exist, then add accordingly.
			 * 5. Have to use this implementation as natty doesn't return 2
			 *    values and doesn't recognize the from...to... format.
			 */

			String dateParsingString = "";
			DateParser dp = new DateParser();
			int fromIdx = -1;
			int toIdx = -1;
			String[] fromConstants = { "from", "start", "starting", "begin", "beginning" };
			String[] toConstants = { "to", "end", "ending", "till", "by" };
			String beforeFrom = "";
			String afterFrom = "";
			String afterTo = "";
			String beforeTo = "";

			for (int j = hexIndex + 1; j < splitString.length; j++) {
				dateParsingString = dateParsingString + " " + splitString[j];
			}
			dateParsingString = dateParsingString.trim();
			String[] dateStringArray = dateParsingString.split(WHITESPACE);

			for (int i = 0; i < dateStringArray.length; i++) {
				for (int j = 0; j < fromConstants.length; j++) {
					String temp1 = dateStringArray[i];
					String temp2 = fromConstants[j];
					if (temp1.equals(temp2)) {
						fromIdx = i;
					}
				}
				for (int k = 0; k < toConstants.length; k++) {
					String temp1 = dateStringArray[i];
					String temp2 = toConstants[k];
					if (temp1.equals(temp2)) {
						toIdx = i;
					}
				}
			}

			if (fromIdx > 0 && toIdx > 0) {
				for (int j = 0; j < fromIdx; j++) {
					beforeFrom = beforeFrom + " " + dateStringArray[j];
				}
				for (int j = fromIdx + 1; j < toIdx; j++) {
					afterFrom = afterFrom + " " + dateStringArray[j];
				}
				for (int j = toIdx + 1; j < dateStringArray.length; j++) {
					afterTo = afterTo + " " + dateStringArray[j];
				}
				beforeFrom = beforeFrom.trim();
				afterFrom = afterFrom.trim();
				afterTo = afterTo.trim();
				startDateForNatty = startDateForNatty + " " + beforeFrom + " " + "at" + " " + afterFrom;
				endDateForNatty = endDateForNatty + " " + beforeFrom + " " + "at" + " " + afterTo;
				startDateForNatty = startDateForNatty.trim();
				endDateForNatty = endDateForNatty.trim();
				parsedStartDate = dp.parse(startDateForNatty);
				parsedEndDate = dp.parse(endDateForNatty);
			} else if (fromIdx > 0 && toIdx < 0){
				for (int j = 0; j < fromIdx; j++) {
					beforeFrom = beforeFrom + " " + dateStringArray[j];
				}
				for (int j = fromIdx + 1; j < dateStringArray.length; j++) {
					afterFrom = afterFrom + " " + dateStringArray[j];
				}
				beforeFrom = beforeFrom.trim();
				afterFrom = afterFrom.trim();
				startDateForNatty = startDateForNatty + " " + beforeFrom + " " + "at" + " " + afterFrom;
				endDateForNatty = "";
				startDateForNatty = startDateForNatty.trim();
				endDateForNatty = endDateForNatty.trim();
				parsedStartDate = dp.parse(startDateForNatty);
				parsedEndDate = dp.parse(endDateForNatty);
			} else if(fromIdx < 0 && toIdx > 0){
				for (int j = 0; j < toIdx; j++) {
					beforeTo = beforeTo + " " + dateStringArray[j];
				}
				for (int j = toIdx + 1; j < dateStringArray.length; j++) {
					afterTo = afterTo + " " + dateStringArray[j];
				}
				beforeTo = beforeTo.trim();
				afterTo = afterTo.trim();
				startDateForNatty = "";
				endDateForNatty = endDateForNatty + " " + beforeTo + " " + "at" + " " + afterTo;
				startDateForNatty = startDateForNatty.trim();
				endDateForNatty = endDateForNatty.trim();
				parsedStartDate = dp.parse(startDateForNatty);
				parsedEndDate = dp.parse(endDateForNatty);
			} else if (fromIdx < 0 && toIdx < 0){
				dateForNatty = dateParsingString;
				parsedDueDate = dp.parse(dateForNatty);
			}

			if (parsedStartDate != null || parsedEndDate != null) {
				op = new AddOperation(todos, new TodoItem(TodoItem.Status.TODO, intPriority, parsedStartDate,
						parsedEndDate, toDoMessage, Frequency.NONE));
			} else if (parsedDueDate != null) {
				op = new AddOperation(todos, new TodoItem(TodoItem.Status.TODO, intPriority, null, parsedDueDate,
						toDoMessage, Frequency.NONE));
			}
			op.execute();
			toDoMessage = "";
			dateForNatty = "";
			startDateForNatty = "";
			endDateForNatty = "";
			parsedDueDate = null;
			parsedStartDate = null;
			parsedEndDate = null;
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
			op = new DeleteOperation(todos, todos.getItem(index - 1));
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
		case "save":
		case "sav":
		case "sv":
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
				todos.toggle(todos.getItem((Integer.parseInt(rest)) - 1));
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
		case "updatem":
		case "upm":
			if (todos.isEmpty()) {
				System.out.printf("No todos\n");
				return;
			}
			if (splitString.length < 2) {
				System.out.println("No number supplied, update not possible");
				index = -1;
			} else {
				try {
					index = Integer.parseInt(splitString[1]);
				} catch (NumberFormatException e) {
					System.out.print("Parameter must be a number\n");
					return;
				}
			}
			String newMsg = "";
			for (int i = 2; i < splitString.length; i++) {
				newMsg = newMsg + " " + splitString[i];
			}
			newMsg = newMsg.trim();
			op = new UpdateMessageOperation(todos, todos.getItem(index - 1), newMsg);
			op.execute();
			break;
		case "updated":
		case "uped":
		case "updd":
		case "updued":
		case "upendd":
			if (todos.isEmpty()) {
				System.out.printf("No todos to update\n");
				return;
			}
			if (splitString.length < 2) {
				System.out.println("No number supplied, update not possible.");
				index = -1;
			} else {
				try {
					index = Integer.parseInt(splitString[1]);
				} catch (NumberFormatException e) {
					System.out.print("Parameter must be a number\n");
					return;
				}
			}
			String newDate = "";
			for (int i = 2; i < splitString.length; i++) {
				newDate += " " + splitString[i];
			}
			newDate = newDate.trim();
			op = new UpdateEndDateOperation(todos, todos.getItem(index - 1), newDate);
			op.execute();
			break;
		case "updatesd":
		case "upsd":
		case "upstartd":
		case "upstd":
			if (todos.isEmpty()) {
				System.out.printf("No todos to update\n");
				return;
			}
			if (splitString.length < 2) {
				System.out.println("No number supplied, update not possible.");
				index = -1;
			} else {
				try {
					index = Integer.parseInt(splitString[1]);
				} catch (NumberFormatException e) {
					System.out.print("Parameter must be a number\n");
					return;
				}
			}
			String newDate2 = "";
			for (int i = 2; i < splitString.length; i++) {
				newDate2 += " " + splitString[i];
			}
			newDate2 = newDate2.trim();
			op = new UpdateStartDateOperation(todos, todos.getItem(index - 1), newDate2);
			op.execute();
			break;
		case "updatep":
		case "updp":
		case "upp":
		case "updatepriority":
			if (todos.isEmpty()) {
				System.out.printf("No todos\n");
				return;
			}
			if (splitString.length < 2) {
				System.out.println("No number supplied, update not possible");
				index = -1;
			} else {
				try {
					index = Integer.parseInt(splitString[1]);
				} catch (NumberFormatException e) {
					System.out.print("Parameter must be a number\n");
					return;
				}
			}
			int newP;
			if (Integer.parseInt(splitString[2]) > 0){
			newP = Integer.parseInt(splitString[2]);
			} else {
				newP = -1;
			}
				
			op = new UpdatePriorityOperation(todos, todos.getItem(index - 1), newP);
			op.execute();
			break;
		case "help":
			System.out.println("Commands");
			System.out.println("display: show the current state of the todo file.");
			System.out.println("add: add a todo item.");
			System.out.println("delete: delete a todo item, specified by the number shown in the display command.");
			System.out.println("clear: delete all the todos in this file.");
			System.out.println("uped/upsd/upm: update the endDate/startDate/message respectively");
			System.out.println("exit: exit the todo file");
			System.out.println("write: force a write to the file");
			System.out.println("sort: sort the todo file by contents.");
			System.out.println("search: search for the contents of a todo item.");
			System.out.println("mode: change the mode, for more info, type 'mode help'");
			System.out.println("whatmode: prints the current display mode");
			System.out.println("time: change the time, for more info, type 'time help'");
			System.out.println("whattime: prints the time setting.");
			System.out.println("previous: change to the previous time chunk.");
			System.out.println("next: change to the next time chunk.");
			System.out.println("todo/done: toggle the status of an item item as todo/done");
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
