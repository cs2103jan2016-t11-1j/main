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
	}

	public void nextCommand(String text) {
		lastCommand = text;
	}

	public void executeCommand() {
		/*
		 * String is split into command, message, priority and dates.
		 */
		Operation op = null;
		DateParser dp = new DateParser();
		String[] splitString = lastCommand.split(WHITESPACE);
		String command = splitString[0];
		command = command.toLowerCase();

		/*
		 * Operations that user enters.
		 * Multiple cases for each command to ensure that some
		 * spelling errors can be checked.  
		 */

		switch (command) {
		/*
		 * Display Operation
		 */
		case "back":
		case "display":
		case "dis":
		case "disp":
		case "dsp":
			op = new DisplayOperation(todos);
			op.execute();
			undos.add(op);
			break;
		/*
		 * Add Operation
		 */
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

			if (hexIndex > 0){
				String hexedPriority = splitString[hexIndex];
				if (!hexedPriority.substring(1).equals("")) {
					intPriority = Integer.parseInt(hexedPriority.substring(1));
				} else {
					intPriority = -1;
				}
			}
			/*
			 * Manual date parsing for the Natty Parser 
			 * to recognize the input. 
			 */

			String dateParsingString = "";
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
			} else {
				op = new AddOperation(todos, new TodoItem(TodoItem.Status.TODO, intPriority, parsedStartDate, null,
						toDoMessage, Frequency.NONE));
			}
			op.execute();
			undos.add(op);
			toDoMessage = "";
			dateForNatty = "";
			startDateForNatty = "";
			endDateForNatty = "";
			parsedDueDate = null;
			parsedStartDate = null;
			parsedEndDate = null;
			flexiView.refresh();
			break;
		/*
		 * Delete Operation
		 */
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
			undos.add(op);
			flexiView.refresh();
			break;
		/*
		 * Clear Operation
		 */
		case "clear":
		case "clr":
		case "cl":
		case "cle":
			// TODO Change to OP
			todos.clear();
			System.out.println("all todos deleted");
			break;
		/*
		 * Exit Operation
		 */
		case "exit":
		case "ext":
		case "ex":
			exit();
			break;
		/*
		 * Save Operation
		 */
		case "write":
		case "wrt":
		case "writ":
		case "save":
		case "sav":
		case "sv":
			// TODO Change to OP
			todos.write();
			break;
		/*
		* Different Sort Operations
		* Sort by contents, priority, dates, Done/Todo tasks
		*/
		case "sortcont":
		case "sortc":
		case "sortcontents":
			todos.sortByContents();
			flexiView.setMode(FlexiArea.Mode.SORT_CONTENTS);
			break;
		case "sortpriority":
		case "sortp":
		case "sortprior":
			todos.sortByPriority();
			flexiView.setMode(FlexiArea.Mode.SORT_PRIORITY);
			break;
		case "sortdate":
		case "sortdd":
		case "sortduedd":
			todos.sortByDueDate();
			flexiView.setMode(FlexiArea.Mode.SORT_DUE_DATE);
			break;
		case "sortstartdate":
		case "sortsd":
		case "sortstartd":
			todos.sortByStartDate();
			flexiView.setMode(FlexiArea.Mode.SORT_START_DATE);
			break;
		case "sortstatus":
		case "sorts":
		case "sortst":
			todos.sortByStatus();
			flexiView.setMode(FlexiArea.Mode.SORT_STATUS);
			break;
		/*
		 * Different PowerSearch Operations:
		 * Search by contents, time, dates, 
		 * next few days, overlaps in events,
		 * free time, priority
		 */
		case "searchstr":
		case "searchs":
		case "srchs":
		case "schstr":
		case "saerchs":
			if (todos.isEmpty()) {
				System.out.printf("No todos to search\n");
				return;
			}
			String searchString = "";
			if (splitString.length < 2) {
				System.out.println("Displaying all, please enter specific text to search");
			} else {

				for (int i = 1; i < splitString.length; i++){
					searchString = searchString + " " + splitString[i];
				}
			}
			flexiView.powerSearchString(searchString.trim());
			break;
		case "searchdate":
		case "searchd":
		case "srchd":
		case "saerchd":
		case "schdate":
			System.out.println("Search Results:");
			if (todos.isEmpty()) {
				System.out.printf("No todos to search\n");
				return;
			}
			String searchDate = "";
			if (splitString.length < 2) {
				System.out.println("Enter specific date text to search");
			} else {
				for (int i = 1; i < splitString.length; i++){
					searchDate = searchDate + " " + splitString[i];
				}
			}
			Date parsedSearchDate = dp.parse(searchDate.trim());
			todos.searchDate(parsedSearchDate);
			flexiView.searchDate(parsedSearchDate);
			break;
		case "searchp":
		case "searchpriority":
		case "srchp":
		case "schp":
		case "saerchp":
			System.out.println("Search Results:");
			if (todos.isEmpty()) {
				System.out.printf("No todos to search\n");
				return;
			}
			int prty = -1;
			int pp = Integer.parseInt(splitString[1]);
			if (splitString.length < 2) {
				System.out.println("Displaying all results, please enter specific text to search");
				prty = -1;
			} else {
				try {
					if (pp < 1){
						prty = -1;
					}else {
						prty = pp;
					}
				} catch (NumberFormatException e) {
					System.out.print("Parameter must be a number\n");
					return;
				}
			}
			flexiView.searchPriority(prty);
			break;
		case "searchfree":
		case "searchf":
		case "srchf":
		case "schfree":
		case "search free":
		case "saerchf":
			System.out.println("Search Results:");
			flexiView.findFreeTime();
			break;
		case "searchclash":
		case "searchc":
		case "srchcl":
		case "schclash":
		case "searchoverlap":
		case "searcho":
		case "saerchc":
			System.out.println("Search Results:");
			flexiView.findOverlap();
			break;
		case "searchnext":
		case "searchn":
		case "srchnxt":
		case "searchnxt":
		case "saerchn":
			System.out.println("Search Results:");
			if (todos.isEmpty()) {
				System.out.printf("No todos to search\n");
				return;
			}
			String now = "now";
			Date searchBlkStartDate = dp.parse(now);
			Date searchBlkEndDate = null; 
			String searchBlkEndDateString = "";
			if (splitString.length < 2) {
				System.out.println("Displaying all, please enter specific text to search");
			}else if (splitString.length == 3){
				searchBlkEndDateString = splitString[1] + " " + splitString[2] + "  from now";
				searchBlkEndDate = dp.parse(searchBlkEndDateString);
			}else {
				// assume days if not specified
				searchBlkEndDateString = splitString[1] + " days from now";
				searchBlkEndDate = dp.parse(searchBlkEndDateString);
			}
			//System.out.println(searchBlkStartDate + "    " +  searchBlkEndDate);
			flexiView.searchInTimeBlock(searchBlkStartDate, searchBlkEndDate);
			searchBlkEndDate = null;
			break;
		/*
		* Switch mode of display
		*/
		case "whatmode":
			switch (flexiView.getMode()) {
			case HEAT_MAP:
				System.out.println("The current mode is Heat map.");
				break;
			case SORT_CONTENTS:
				System.out.println("The current mode is Sort Contents.");
				break;
			case SORT_DUE_DATE:
				System.out.println("The current mode is Sort Due Date.");
				break;
			case SORT_START_DATE:
				System.out.println("The current mode is Sort Start Date.");
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
			case "duedate":
				flexiView.setMode(FlexiArea.Mode.SORT_DUE_DATE);
				break;
			case "startdate":
				flexiView.setMode(FlexiArea.Mode.SORT_START_DATE);
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
				System.out.println("The current time mode is All.");
				break;
			case DAY:
				System.out.println("The current time mode is day from " + flexiView.start() + " to " + flexiView.end());
				break;
			case FUTURE:
				System.out.println(
						"The current time mode is future from " + flexiView.start() + " onwards.");
				break;
			case MONTH:
				System.out
				.println("The current time mode is month from " + flexiView.start() + " to " + flexiView.end());
				break;
			case WEEK:
				System.out
				.println("The current time mode is week from " + flexiView.start() + " to " + flexiView.end());
				break;
			case FLOATING:
				System.out.println("The current time mode is Floating.");
				break;
			default:
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
			case "float":
			case "floating":
				flexiView.setTimeState(FlexiArea.TimeState.FLOATING);
				System.out.println("Set time interval to floating");
				break;
			default:
				System.out.println("Time chunk not recognized.");
				break;
			}
			break;
		/*
		 * Display floating tasks operation
		 */
		case "float":
		case "floating":
			flexiView.setTimeState(FlexiArea.TimeState.FLOATING);
			System.out.println("Set time interval to floating");
			break;
		/*
		 * Mark as done/todo Operation
		 */
		case "todo":
		case "done":
			if (todos.isEmpty()) {
				System.out.printf("No todos\n");
				return;
			}
			Integer indx = null;
			if (splitString.length < 2) {
				System.out.println("No number supplied");
				index = 1;
			} else {
				try {
					indx = Integer.parseInt(splitString[1].trim());
				} catch (NumberFormatException e) {
					System.out.print("Parameter must be a number\n");
					return;
				}
			}
			todos.toggle(todos.getItem(indx - 1));
			flexiView.refresh();
			break;
		case "previous":
		case "prev":
			flexiView.previousTimeChunk();
			break;
		case "next":
		case "nxt":
			flexiView.nextTimeChunk();
			break;
		/*
		 * Update Operations
		 */
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
			undos.add(op);
			flexiView.refresh();
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
			undos.add(op);
			flexiView.refresh();
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
			undos.add(op);
			flexiView.refresh();
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
			undos.add(op);
			flexiView.refresh();
			break;
		/*
		 * Help for user Operation
		 */
		case "help":
		case "/":
			System.out.println("Commands");
			System.out.println("display: show the current state of the todo file.");
			System.out.println("add: add a todo item.");
			System.out.println("delete: delete a todo item, specified by the number shown in the display command.");
			System.out.println("clear: delete all the todos in this file.");
			System.out.println("uped/upsd/upm: update the endDate/startDate/message respectively");
			System.out.println("exit: exit the todo file");
			System.out.println("write: force a write to the file");
			System.out.println("sortc: sort the todo file by contents.");
			System.out.println("sortp: sort the todo file by priority.");
			System.out.println("sortsd: sort the todo file by start date.");
			System.out.println("sortdd: sort the todo file by due/end date.");
			System.out.println("searchs: search by contents of a todo item.");
			System.out.println("searchd: search by date of a todo item.");
			System.out.println("searchp: search by priority of a todo item.");
			System.out.println("searchc: search for clashes/overlaps in the todo items.");
			System.out.println("searchf: search for free time/empty slots");
			System.out.println("searchn: search by next 'N hour(s)/day(s)/week(s)/month(s)'");
			System.out.println("mode: change the mode, for more info, type 'mode help'");
			System.out.println("whatmode: prints the current display mode");
			System.out.println("time: change the time, for more info, type 'time help'");
			System.out.println("whattime: prints the time setting.");
			System.out.println("previous: change to the previous time chunk.");
			System.out.println("next: change to the next time chunk.");
			System.out.println("todo/done: toggle the status of an item item as todo/done");
			break;
		/*
		 * Undo Operation
		 */
		case "undo":
		case "un":
		case "ud":
			undos.undo();
			flexiView.refresh();
			break;
		/*
		 * Change directory Operation
		 */
		case "cd":
			String rest = getRest(command).trim();
			op = new ChangeDirectoryOperation(todos, rest, this);
			op.execute();
			undos.add(op);
			flexiView.refresh();
			break;
		case "donelast":
			flexiView.toggleDoneLast();
			break;
		default:
			System.out.println("Command not recognized.");
			break;
		}
	}
	public void setTodoFile(TodoFile t) {
		todos = t;
		flexiView.setTodos(t);
	}

	public String getLastCommand() {
		return lastCommand;
	}

	private String getRest(String command) {
		return lastCommand.substring(command.length());
	}
	public void exit() {
		todos.exit();
		System.exit(0);
	}

}
