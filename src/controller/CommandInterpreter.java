package controller;

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
         * String is split into command, message,
         * priority and date and stored in splitString[]
         *
         */
        Operation op = null;
        String[] splitString = lastCommand.split(WHITESPACE);
        String command = splitString[0];

        //TODO make the command ignore case
        switch (command) {
        case "display":
            op = new DisplayOperation(todos);
            op.execute();
            break;
        case "add":
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
            String rest = lastCommand.substring(command.length()).replaceAll(WHITESPACE, "");
            todos.searchString(rest);
            break;
        case "mode":
            String newMode = lastCommand.substring(command.length()).trim();
            switch (newMode) {
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
        case "time":
            String newTime = lastCommand.substring(command.length()).trim();
            switch (newTime) {
            case "day":
                flexiView.setTimeState(FlexiArea.TimeState.DAY);
                break;
            case "week":
                flexiView.setTimeState(FlexiArea.TimeState.WEEK);
                break;
            case "month":
                flexiView.setTimeState(FlexiArea.TimeState.MONTH);
                break;
            case "all":
                flexiView.setTimeState(FlexiArea.TimeState.ALL);
                break;
            case "future":
                flexiView.setTimeState(FlexiArea.TimeState.FUTURE);
                break;
            default:
                System.out.println("Time chunk not recognized not recognized.");
                break;
            }
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
