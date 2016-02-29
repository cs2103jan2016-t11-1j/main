import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class CommandInterpreter {
    private static final boolean APPEND_TO_FILE = false;
    private static final String WHITESPACE = "\\s+"; // whitespace regex
    private String lastCommand;
    private BufferedReader in;
    private ArrayList<String> fileState;
    private PrintWriter fileWriter;
    private String fileName;

    public CommandInterpreter(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName, APPEND_TO_FILE);
            this.fileWriter = new PrintWriter(writer);
        } catch (IOException e) {
            System.err.printf("Could not open file %s", fileName);
            System.exit(1);
        }
        lastCommand = null;
        in = new BufferedReader(new InputStreamReader(System.in));
        fileState = new ArrayList<String>();
        this.fileName = fileName;
    }

    public void nextCommand() {
        try {
            lastCommand = in.readLine();
        } catch (IOException io) {
            io.printStackTrace();
            exit();
            System.exit(0);
        }
    }

    public void executeCommand() {
        String[] splitString = lastCommand.split(WHITESPACE);
        String command = splitString[0];

        switch (command) {
        case "display":
            if (this.empty()) {
                System.out.printf("%s is empty\n", fileName);
                return;
            }
            printFile();
            break;
        case "add":
            //assuming the whitespace between the command and what is to be added is not significant
            String rest = lastCommand.substring(command.length()).replaceAll("^\\s+", "");
            fileState.add(rest);
            System.out.printf("added to %s: \"%s\"\n", fileName, rest);
            break;
        case "delete":
            if (this.empty()) {
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
            } break;
        case "clear":
            fileState.clear();
            System.out.printf("all content deleted from %s\n", fileName);
            break;
        case "exit":
            exit();
            System.exit(0);
            break;
        case "write":
            write();
            break;
        case "sort":
            Collections.sort(fileState);
            printFile();
            break;
        case "search":
            rest = lastCommand.substring(command.length()).replaceAll("^\\s+", "");

            int i = 1;
            for (String s: fileState) {
                if (s.contains(rest)) {
                    this.printLine(i);
                }
                i++;
            }
            break;
        default:
            System.out.print("Command not recognized.\n");
            break;
        }

    }

    public boolean empty() {
        return fileState.size() == 0;
    }

    public void exit() {
        write();
        fileWriter.close();
    }

    private void write() {
        for (int i = fileState.size(); i > 0; i--) {
            fileWriter.println(fileState.remove(0));
        }
        fileWriter.flush();
        System.out.printf("Wrote to %s.\n", fileName);
    }

    private void printFile() {
        for (int i = 1; i < fileState.size() + 1; i++) {
            printLine(i);
        }
    }
    private void printLine(int location) {
        System.out.printf("%d. %s\n", location, fileState.get(location - 1));
    }

    public String getLastCommand() {
        return lastCommand;
    }
    protected void setLastCommand(String last) {
        this.lastCommand = last;
    }
    protected String getEntry(int i) {
        return fileState.get(i);
    }
}
