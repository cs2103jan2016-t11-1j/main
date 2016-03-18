package controller;
import model.TodoFile;
import model.TodoItem;
import view.FlexiArea;

public class CommandInterpreter {
    private static final String WHITESPACE = "\\s+"; // whitespace regex
    private String lastCommand;
    private TodoFile todos;
	private FlexiArea flexiView;
	private Undoer undos;

    public CommandInterpreter(TodoFile todos, FlexiArea flexiView) {
    	this.todos = todos;
    	this.lastCommand = null;
    	this.flexiView = flexiView;
    	undos = new Undoer();
    	updateFlexiView();
	}

	public void nextCommand(String text) {
		lastCommand = text;
	}

    public void executeCommand() {
        //
        // TODO: transfer most of this stuff to the TodoFile class
        //
        String[] splitString = lastCommand.split(WHITESPACE);
        String command = splitString[0];
        Operation op = null;
        switch (command) {
        case "display":
        	op = new DisplayOperation(todos);
        	op.execute();
            break;
        case "add":
        	//TODO update with new formalism to match display
            //assuming the whitespace between the command and what is to be added is not significant
            String rest = lastCommand.substring(command.length()).replaceAll("^\\s+", "");
            //TODO change this to parse the todo entry
            todos.add(rest);
            break;
        case "delete":
        	//TODO update with new formalism to match display
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
        	//TODO update with new formalism to match display
            todos.clear();
            System.out.println("all todos deleted");
            break;
        case "exit":
        	//TODO update with new formalism to match display
            exit();
            System.exit(0);
            break;
        case "write":
        	//TODO update with new formalism to match display
            todos.write();
            break;
        case "sort":
        	//TODO update with new formalism to match display
            todos.sortByContents();
            System.out.println("sorted by Contents"); 
            break;
        case "search":
        	//TODO update with new formalism to match display
            rest = lastCommand.substring(command.length()).replaceAll("^\\s+", "");
            todos.searchString(rest);
            break;
        default:
            System.out.print("Command not recognized.\n");
            break;
        }
        undos.add(op);
        updateFlexiView();
    }

    private void updateFlexiView() {
    	this.flexiView.clear();
    	this.flexiView.println(todos);
	}
    public void undo() {
    	undos.undo();
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
    public void exit() {
        todos.exit();
        System.exit(0);
    }


}
