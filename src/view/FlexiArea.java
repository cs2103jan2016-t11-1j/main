package view;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.TodoFile;
import model.TodoItem;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.time.Duration;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Collections;

//import java.time;

//TODO make this just a box, not a text area
public class FlexiArea extends TextFlow {
	public static final Mode DEFAULT_MODE = Mode.SORT_DATE;
	public static final TimeState DEFAULT_TIME = TimeState.ALL;
	private TodoFile todos;
	private Mode mode = DEFAULT_MODE;
	private TimeState timeState;
	private LocalDateTime start, end;
	private List<TodoItem> currTodos;

	public enum Mode {
		/*
		 * SORT_DATE: Show the due dates SORT_PRIORITY: Sort by priority
		 * SORT_STATUS: Sort by status SORT_CONTENTS: Sort by contents HEAT_MAP:
		 * show the heat map
		 */
		SORT_DATE, SORT_PRIORITY, SORT_STATUS, SORT_CONTENTS, HEAT_MAP
	};

	public enum TimeState {
		/*
		 * This is the chunk of time shown. DAY: view each day WEEK: view each
		 * week MONTH: view each month ALL: everything, in a big bag FUTURE:
		 * everything in the future, in a big bag
		 */
		DAY, WEEK, MONTH, ALL, FUTURE
	}

	public FlexiArea(TodoFile info) {
		this.todos = info;
		setTimeState(DEFAULT_TIME);
		currTodos = new ArrayList<TodoItem>();
	}

	/*
	 * updates the dates one more time chunk
	 */
	public void nextTimeChunk() {
		switch (timeState) {
		case DAY:
			start = end;
			end = start.plus(Duration.ofDays(1));
			break;
		case WEEK:
			start = end;
			end = start.plus(Duration.ofDays(7));
			break;
		case MONTH:
			start = end;
			end = start.with(TemporalAdjusters.firstDayOfNextMonth());
			break;
		case FUTURE:
			start = LocalDate.now().atStartOfDay();
		default:
			break;
		}
	}

	public void previousTimeChunk() {
		switch (timeState) {
		case DAY:
			end = start;
			start = end.minus(Duration.ofDays(1));
			break;
		case WEEK:
			end = start;
			start = end.minus(Duration.ofDays(7));
			break;
		case MONTH:
			end = start;
			start = end.minus(Duration.ofDays(1)).with(TemporalAdjusters.firstDayOfMonth());
			break;
		case FUTURE:
			start = LocalDate.now().atStartOfDay();
		default:
			break;
		}
	}

	public void setTimeState(TimeState timeState) {
		if (start == null) {
			start = LocalDate.now().atStartOfDay();
		}
		if (end == null) {
			end = LocalDate.now().atStartOfDay();
		}
		if (start.getYear() == -999999999) {
			start = LocalDate.now().atStartOfDay();
		}
		switch (timeState) {
		case DAY:
			end = start.plus(Duration.ofDays(1));
			break;
		case WEEK:
			start = start.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
			end = start.plus(Duration.ofDays(7));
			break;
		case MONTH:
			start = start.with(TemporalAdjusters.firstDayOfMonth());
			end = start.with(TemporalAdjusters.firstDayOfNextMonth());
			break;
		case ALL:
			start = LocalDateTime.MIN;
			end = LocalDateTime.MAX;
			break;
		case FUTURE:
			start = LocalDate.now().atStartOfDay();
			end = LocalDateTime.MAX;
			break;
		}

		this.timeState = timeState;
		refresh();
	}

	public void refresh() {
		setMode(mode);
	}

	public void setMode(Mode newState) {
		this.getChildren().clear();
		List<TodoItem> startTodos = todos.filterStartDates(start, end);
		List<TodoItem> dueTodos = todos.filterDueDates(start, end);
		switch (newState) {
		case SORT_PRIORITY:
			Collections.sort(startTodos, TodoItem.getPriorityComparator());
			Collections.sort(dueTodos, TodoItem.getPriorityComparator());
			break;
		case SORT_CONTENTS:
			Collections.sort(startTodos, TodoItem.getContentsComparator());
			Collections.sort(dueTodos, TodoItem.getContentsComparator());
			break;
		case SORT_DATE:
		case HEAT_MAP:
			Collections.sort(startTodos, TodoItem.getStartDateComparator());
			Collections.sort(dueTodos, TodoItem.getDueDateComparator());
			break;
		case SORT_STATUS:
			Collections.sort(startTodos, TodoItem.getStatusComparator());
			Collections.sort(dueTodos, TodoItem.getStatusComparator());
			break;

		}
		currTodos = new ArrayList<TodoItem>();
		currTodos.addAll(dueTodos);
		currTodos.addAll(startTodos);
		printDueTodos(dueTodos);
		printStartTodos(startTodos);

		this.mode = newState;
	}

	public void setTodos(TodoFile todos) {
		this.todos = todos;
		refresh();
	}
	public void printStartTodos(List<TodoItem> startTodos) {
		List<Node> children = this.getChildren();
		for (TodoItem t : startTodos) {
			Text txt;
			if (t.isDone()) {
				txt = new Text("DONE, Start date at " + t.getStartDate() + " || " + t.getContents() + "\n");
				txt.setFill(Color.BLUE);
			} else {
				txt = new Text("TODO, Start date at " + t.getStartDate() + " || " + t.getContents() + "\n");
				txt.setFill(Color.RED);
			}
			children.add(txt);
		}
	}

	public void printDueTodos(List<TodoItem> dueTodos) {
		List<Node> children = this.getChildren();
		for (TodoItem t : dueTodos) {
			Text txt;
			if (t.isDone()) {
				txt = new Text("DONE, Start date at " + t.getDueDate() + " || " + t.getContents() + "\n");
				txt.setFill(Color.BLUE);
			} else {
				txt = new Text("TODO, Start date at " + t.getDueDate() + " || " + t.getContents() + "\n");
				txt.setFill(Color.RED);
			}
			children.add(txt);
		}
	}
	public void println(String s) {
		this.getChildren().add(new Text(s));
	}
	public Mode getMode() {
		return mode;
	}
	public TodoItem getNthTodo(int i) {
		return currTodos.get(i);
	}
	public String start() {
		return start.toString();
	}

	public String end() {
		return end.toString();
	}

	public TimeState getTimeState() {
		return timeState;
	}
}