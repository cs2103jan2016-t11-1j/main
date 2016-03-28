package view;
import java.io.PrintStream;

import controller.CommandInterpreter;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.TodoFile;

public class TidyYard extends Application {
    private static final String DEFAULT_FILE = "main.todo";
    private static final int HWIDTH = 60;
    private static final int VWIDTH = 60;
    private CommandInterpreter controller;
    private TodoFile todos;
    private TextField inputField;
    private TextArea commandResult;
    private FlexiArea flexiView;
    private GridPane grid;

    public static void main(String[] args) {
        launch(args);
    }
    private void addTopInput() {
    	this.inputField = new TextField();
    	this.grid.add(this.inputField, 0, 0, HWIDTH, 1);
    }
    private void addCommandResult() {
    	this.commandResult = new TextArea();
    	this.commandResult.setEditable(false);

    	this.grid.add(this.commandResult, 0, 1, 1, VWIDTH - 1);
    }
    private void addFlexiView() {
    	this.flexiView = new FlexiArea(todos);
    	this.flexiView.setEditable(false);
    	this.grid.add(this.flexiView, 1, 1, 1, VWIDTH - 1);
    }
    private void redirect() {
    	PrintStream stream = new PrintStream(new ConsoleArea(this.commandResult), true);
    	System.setOut(stream);
    	System.setErr(stream);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(0);
        grid.setVgap(10);
        //grid.setPadding(new Insets(25, 25, 25, 25));
        addTopInput();
        addCommandResult();
        todos = new TodoFile(DEFAULT_FILE);
        addFlexiView();
        controller = new CommandInterpreter(todos, flexiView);




        //     commander.nextCommand();
        //     commander.executeCommand();
        //     System.out.print(PROMPT);
        // }

        Scene scene = new Scene(grid);    	
        scene.setOnKeyPressed(new EventHandler<KeyEvent> () {
    		@Override
    		public void handle(KeyEvent e) {
    			if (e.getCode().equals(KeyCode.ENTER)) {
        			controller.nextCommand(inputField.getText());
        			controller.executeCommand();
        			inputField.clear();
    			}
    		}
    	});
        //scene.getStylesheets().add(TidyYard.class.getResource("resources/styles/test.css").toExternalForm());
        primaryStage.setTitle("Tidy Yard");
        primaryStage.setScene(scene);
        primaryStage.show();
        redirect();
    }
    
    @Override
    public void stop() {
    	controller.exit();
    }
}
