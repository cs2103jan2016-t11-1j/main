package view;
/**
 * @@author A0149108E
 */
import java.io.PrintStream;

import controller.CommandInterpreter;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.TodoFile;

public class TidyYard extends Application {
	private static final String DEFAULT_FILE = "main.todo";
	private CommandInterpreter controller;
	private TodoFile todos;
	private TextField inputField;
	private TextArea commandResult;
	private FlexiArea flexiView;
	private ScrollPane scroll;
	private BorderPane border;

	public static void main(String[] args) {
		launch(args);
	}

	private void addTopInput() {
		this.inputField = new TextField();
		this.inputField.setId("input");
		this.border.setTop(this.inputField);
	}

	private void addCommandResult() {
		this.commandResult = new TextArea();
		this.commandResult.setEditable(false);
		this.commandResult.setVisible(true);
		this.commandResult.setId("console");
		this.border.setBottom(this.commandResult);
	}

	private void addFlexiView() {
		this.flexiView = new FlexiArea(todos);
		this.flexiView.setVisible(true);
		this.flexiView.setId("flexi-view");
		this.scroll = new ScrollPane();
		this.scroll.setContent(this.flexiView);
		this.border.setCenter(this.scroll);
	}

	private void redirect() {
		PrintStream stream = new PrintStream(new ConsoleArea(commandResult), true);
		System.setOut(stream);
	}	
	
	private void setScene(Stage primaryStage) {
		Scene scene = new Scene(border, 1000, 800, Color.BLACK);
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode().equals(KeyCode.ENTER)) {
					controller.nextCommand(inputField.getText());
					controller.executeCommand();
					inputField.clear();
				}
			}
		});
		primaryStage.setTitle("Tidy Yard");
		primaryStage.setScene(scene);
		primaryStage.show();
		redirect();
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		border = new BorderPane();
		addTopInput();
		addCommandResult();
		todos = new TodoFile(DEFAULT_FILE);
		addFlexiView();
		controller = new CommandInterpreter(todos, scroll);

		setScene(primaryStage);
	}

	@Override
	public void stop() {
		controller.exit();
	}
}
