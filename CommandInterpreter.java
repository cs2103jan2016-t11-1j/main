import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandInterpreter {
    private static final String WHITESPACE = "\\s+"; // whitespace regex
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
        String[] splitString = lastCommand.split(WHITESPACE);
        String command = splitString[0];

        switch (command) {
        case "display":
            todos.display();
            break;
        case "add":
            //assuming the whitespace between the command and what is to be added is not significant
            String rest = lastCommand.substring(command.length()).replaceAll("^\\s+", "");
            //TODO change this to parse the todo entry
            todos.add(rest);
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
}
