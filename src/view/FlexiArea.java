package view;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.TodoFile;
import model.TodoItem;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class FlexiArea extends TextFlow {
	public static final Mode DEFAULT_MODE = Mode.SORT_START_DATE;
	public static final TimeState DEFAULT_TIME = TimeState.ALL;
	public static final int PAD_SIZE = 46;
	private TodoFile todos;
	private Mode mode = DEFAULT_MODE;
	private TimeState timeState;
	private LocalDateTime start, end; // null when floating
	private List<GuiTodo> currTodos;
	private boolean doneLast;



	public enum Mode {
		/*
		 * SORT_DATE: Show the due dates SORT_PRIORITY: Sort by priority
		 * SORT_STATUS: Sort by status SORT_CONTENTS: Sort by contents HEAT_MAP:
		 * show the heat map
		 */
		SORT_START_DATE, SORT_DUE_DATE, SORT_PRIORITY, SORT_STATUS, SORT_CONTENTS, HEAT_MAP
	};

	public enum TimeState {
		/*
		 * This is the chunk of time shown. DAY: view each day WEEK: view each
		 * week MONTH: view each month ALL: everything, in a big bag FUTURE:
		 * everything in the future, in a big bag
		 */
		DAY, WEEK, MONTH, ALL, FUTURE, FLOATING
	}

	public FlexiArea(TodoFile info) {
		this.todos = info;
		setTimeState(DEFAULT_TIME);
		currTodos = new ArrayList<GuiTodo>();
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
			break;
		case FLOATING:
			break;
		case ALL:
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
			break;
		case FLOATING:
			break;
		case ALL:
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
		case FLOATING:
			break;
		}
		this.timeState = timeState;
		refresh();
	}

	public void refresh() {
		setMode(mode);
	}

	private List<GuiTodo> toGuiTodos(List<TodoItem> todos, GuiTodo.SoD status) {
		List<GuiTodo> ret = new ArrayList<GuiTodo>();
		for (TodoItem todo : todos) {
			ret.add(new GuiTodo(todo, status));
		}
		return ret;
	}

	public void setMode(Mode newState) {
		this.getChildren().clear();
		if (timeState == TimeState.FLOATING) {
			currTodos = toGuiTodos(todos.filterFloatingTodos(), GuiTodo.SoD.FLOATING);
			switch (newState) {
			case SORT_PRIORITY:
				Collections.sort(currTodos, GuiTodo.getPriorityComparator());
				break;
			case SORT_CONTENTS:
				Collections.sort(currTodos, GuiTodo.getContentsComparator());
				break;
			case SORT_START_DATE:
			case HEAT_MAP:
			case SORT_DUE_DATE:
				break;
			case SORT_STATUS:
				Collections.sort(currTodos, GuiTodo.getStatusComparator());
				break;
			}
		} else {
			if (timeState != TimeState.ALL) {
				println("Starting " + start + "\n");
			}
			List<GuiTodo> startTodos = toGuiTodos(todos.filterStartDates(start, end), GuiTodo.SoD.START);
			List<GuiTodo> dueTodos = toGuiTodos(todos.filterDueDates(start, end), GuiTodo.SoD.DUE);

			switch (newState) {
			case SORT_PRIORITY:
				Collections.sort(startTodos, GuiTodo.getPriorityComparator());
				Collections.sort(dueTodos, GuiTodo.getPriorityComparator());
				break;
			case SORT_CONTENTS:
				Collections.sort(startTodos, GuiTodo.getContentsComparator());
				Collections.sort(dueTodos, GuiTodo.getContentsComparator());
				break;
			case SORT_START_DATE:
			case SORT_DUE_DATE:
			case HEAT_MAP:
				Collections.sort(startTodos, GuiTodo.getStartDateComparator());
				Collections.sort(dueTodos, GuiTodo.getDueDateComparator());
				break;
			case SORT_STATUS:
				break;
			}
			currTodos = new ArrayList<GuiTodo>();
			currTodos.addAll(dueTodos);
			currTodos.addAll(startTodos);
		}
		if (doneLast) { Collections.sort(currTodos, GuiTodo.getStatusComparator());	}
		printGuiTodos(currTodos);
		if (timeState == TimeState.DAY || timeState == TimeState.WEEK || timeState == TimeState.MONTH) {
			println("Ending " + start + "\n");
		}
		this.mode = newState;
	}

	private void printGuiTodos(List<GuiTodo> todos) {
		List<Node> children = this.getChildren();
		for (GuiTodo t : todos) {
			Text txt;
			TodoItem todo = t.getTodo();
			if (t.getStatus() == GuiTodo.SoD.FLOATING) {
				txt = new Text(todo.getStatus().name() + " || " + todo.getContents() + "\n");
				if (todo.isDone()) {
					txt.setFill(Color.BLUE);
				} else {				
					txt.setFill(Color.GREEN);
				}
			} else {
				String ret = todo.getStatus().name() + ", " + 
							 t.getStatus().name() + " date at " + 
							 todo.getDueDate();
				txt = new Text(leftPad(ret, PAD_SIZE) + " || " + todo.getContents() + "\n");
				txt.setFont(Font.font("monospaced"));
				if (todo.isDone()) {
					txt.setFill(Color.BLUE);
				} else {
					if (todo.getDueDate() != null && Instant.now().isAfter(todo.getDueDate().toInstant())) {
						txt.setFill(Color.RED);
					} else {
						txt.setFill(Color.GREEN);
					}
				}
			}
			children.add(txt);
		}
	}

	private static String leftPad(String s, int pad) {
		if (s.length() < pad) {
			return s.concat(repeat(pad - s.length()));
		}
		return s;
	}

	private static String repeat(int count, String with) {
		return new String(new char[count]).replace("\0", with);
	}

	private static String repeat(int count) {
		return repeat(count, " ");
	}

	public void println(String s) {
		this.getChildren().add(new Text(s));
	}

	public Mode getMode() {
		return mode;
	}

	public TodoItem getNthTodo(int i) {
		return currTodos.get(i).getTodo();
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

	public void powerSearchString(String trim) {
		todos.powerSearchString(trim);
	}

	public void searchDate(Date parsedSearchDate) {
		todos.searchDate(parsedSearchDate);

	}

	public void searchPriority(int prty) {
		todos.searchPriority(prty);
	}

	public void findFreeTime() {
		todos.findFreeTime();

	}

	public void findOverlap() {
		todos.findOverlap();
	}

	public void setTodos(TodoFile todos) {
		this.todos = todos;
		refresh();
	}

	public boolean isDoneLast() {
		return doneLast;
	}

	public void toggleDoneLast() {
		this.doneLast = !this.doneLast;
		refresh();
	}

	public void searchInTimeBlock(Date searchBlkStartDate, Date searchBlkEndDate) {
		todos.searchInTimeBlock(searchBlkStartDate, searchBlkStartDate);
	}
}