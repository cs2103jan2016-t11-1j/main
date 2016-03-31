package view;

import java.io.PrintStream;

import controller.CommandInterpreter;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.TodoFile;

public class TidyYard extends Application {
	private static final String DEFAULT_FILE = "main.todo";
	private static final int HWIDTH = 10;
	private static final int VWIDTH = 10;
	private CommandInterpreter controller;
	private TodoFile todos;
	private TextField inputField;
	private TextArea commandResult;
	private FlexiArea flexiView;
	private BorderPane border;

	public static void main(String[] args) {
		launch(args);
	}

	private void addTopInput() {
		this.inputField = new TextField();
		this.border.setTop(this.inputField);
	}

	private void addCommandResult() {
		this.commandResult = new TextArea();
		this.commandResult.setEditable(false);
		this.border.setBottom(this.commandResult);
	}

	private void addFlexiView() {
		this.flexiView = new FlexiArea(todos);
		this.flexiView.setVisible(true);
		this.border.setBottom(this.flexiView);
	}

	private void redirect() {
		PrintStream stream = new PrintStream(new ConsoleArea(this.commandResult), true);
		// System.setOut(stream);
		// System.setErr(stream);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		border = new BorderPane();
		// border.setHgap(0);
		// border.setVgap(3);
		// grid.setPadding(new Insets(25, 25, 25, 25));
		addTopInput();
		addCommandResult();
		todos = new TodoFile(DEFAULT_FILE);
		addFlexiView();
		controller = new CommandInterpreter(todos, flexiView);

		Scene scene = new Scene(border, 1000, 800, Color.WHITE);
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
		// scene.getStylesheets().add(TidyYard.class.getResource("resources/styles/test.css").toExternalForm());
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
