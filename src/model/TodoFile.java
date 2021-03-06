package model;

import java.io.*;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.text.ParseException;
import java.util.List;

import model.TodoItem.Frequency;
import model.TodoItem.Status;

public class TodoFile {
	private static final String SPLITTER = "\\|";
	private static final DateFormat FORMATTER = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	private String fileName;
	private List<TodoItem> todos;
	private int lines; // line is indexed at 1
	private PrintWriter writer = null;

	/**
	 * @param fileName the path to the todo file
	 *
	 * @@author A0149108E
	 */
	public TodoFile(String fileName) {
		this.fileName = fileName;
		this.lines = 1;
		this.todos = new ArrayList<TodoItem>();
		if (new File(fileName).exists()) {
			readTodos(fileName);
		}
		updateRecur();
	}

	/**
	 * @param path the path to the todo file
	 * @@author A0135747X
	 */
	public void readTodos(String path) {
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String currLine;
			while ((currLine = reader.readLine()) != null) {
				TodoItem t = parseTodo(currLine);
				assert t != null;
				todos.add(t);
				lines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param todo String
	 * @return parsed
	 * @@author A0149108E
	 */
	private TodoItem parseTodo(String todo) {
		String[] parts = todo.split(SPLITTER);
		if (parts.length < 6) {
			System.err.println(todo);
			System.err.println(parts[2]);
			System.err.println(parts.length);
			error("Wrong number of sections", lines);
		}
		// status,prior,start,due,freq,content
		TodoItem.Status s = todoStatusParsing(parts);
		int p = todoPriorityParsing(parts);
		Date sD = todoStartDateParsing(parts);
		Date dD = todoEndDateParsing(parts);
		Frequency f = todoFrequencyParsing(parts);
		String c = todoContentParsing(parts);
		return new TodoItem(s, p, sD, dD, c, f);
	}

	private String todoContentParsing(String[] parts) {
		String c = "";
		for (int i = 5; i < parts.length; i++) {
			if (i == 5) {
				c += parts[i];
			} else {
				c += ("|" + parts[i]);
			}
		}
		return c;
	}

	private Frequency todoFrequencyParsing(String[] parts) {
		Frequency f = null;
		if (parts[4].equals("")) {
			f = Frequency.NONE;
		} else if (parts[4].equals("DAILY")) {
			f = Frequency.DAILY;
		} else if (parts[4].equals("WEEKLY")) {
			f = Frequency.WEEKLY;
		} else if (parts[4].equals("MONTHLY")) {
			f = Frequency.MONTHLY;
		} else if (parts[4].equals("YEARLY")) {
			f = Frequency.YEARLY;
		} else {
			System.out.println(parts[4]);
			error("Error parsing frequency", lines);
		}
		return f;
	}

	private Date todoEndDateParsing(String[] parts) {
		Date dD = null;
		try {
			dD = parts[3].isEmpty() ? null : (Date) FORMATTER.parse(parts[3]);
		} catch (ParseException e) {
			e.printStackTrace();
			error("Error parsing due date", lines);
		}
		return dD;
	}

	private Date todoStartDateParsing(String[] parts) {
		Date sD = null;
		try {
			sD = parts[2].isEmpty() ? null : (Date) FORMATTER.parse(parts[2]);
		} catch (ParseException e) {
			e.printStackTrace();
			error("Error parsing start date", lines);
		}
		return sD;
	}

	private int todoPriorityParsing(String[] parts) {
		int p = -1;
		try {
			p = parts[1].isEmpty() ? -1 : Integer.parseInt(parts[1]);
		} catch (Exception e) {
			error("Priority is not a valid int", lines);
		}
		return p;
	}

	private TodoItem.Status todoStatusParsing(String[] parts) {
		TodoItem.Status s = null;
		if (parts[0].equals("TODO")) {
			s = TodoItem.Status.TODO;
		} else if (parts[0].equals("DONE")) {
			s = TodoItem.Status.DONE;
		} else {
			error("Status: " + parts[0] + " is incorrect format", lines);
		}
		return s;
	}

	/**
	 * @param msg error message
	 * @param l line number
	 * @@author A0149108E
	 */
	private void error(String msg, int l) {
		System.err.print(msg + " at line " + l);
		System.exit(1);
	}

	/**
	 * @@author A0149108E
	 */
	public void delete(TodoItem t) {
		try {
			todos.remove(t);
			System.out.printf("deleted from %s: \"%s\"\n", fileName, t.toString());
		} catch (Exception e) {
			System.out.printf(t.toString() + " is not found in todos.");
		}
		todos.remove(t);
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void searchDate(Date toFind) {
		TodoDateSearcher tdds = new TodoDateSearcher();
		tdds.searchDate(todos, toFind);
	}

	/**
	 * @@author A0135747X
	 */
	public void searchString(String toFind) {
		TodoStringSearcher tdss = new TodoStringSearcher();
		tdss.searchString(todos, toFind);
	}

	/**
	 * @@author A0135747X
	 */
	public void searchTime(Date toFind) {
		TodoTimeSearcher tdts = new TodoTimeSearcher();
		tdts.searchTime(todos, toFind);
	}

	/**
	 * @@author A0149108E
	 */
	public void display() {
		if (this.isEmpty()) {
			System.out.println(fileName + " is empty");
			return;
		}
		printFile();
	}


	/**
	 * Adds a TodoItem to the <tt>todos</tt> List with the parameter,
	 * <tt>message</tt>, as the content.
	 * 
	 * @param message
	 *            that the user wants to store
	 * @@author A0149108E
	 */
	public void add(String message) {
		todos.add(new TodoItem(TodoItem.Status.TODO, -1, null, null, message, Frequency.NONE));
		System.out.printf("added to %s: \"%s\"\n", fileName, message);
	}

	/**
	 * @@author A0135747X
	 */
	public void add(int priority, String message) {
		todos.add(new TodoItem(TodoItem.Status.TODO, priority, null, null, message, Frequency.NONE));
		System.out.printf("added to %s: \"%s\"\n", fileName, message);
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void add(int priority, Date startDate, String message) {
		todos.add(new TodoItem(TodoItem.Status.TODO, priority, startDate, null, message, Frequency.NONE));
		System.out.printf("added to %s: \"%s\"\n", fileName, message);
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void add(int priority, String message, Date dueDate) {
		todos.add(new TodoItem(TodoItem.Status.TODO, priority, null, dueDate, message, Frequency.NONE));
		System.out.printf("added to %s: \"%s\"\n", fileName, message);
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void add(int priority, Date startDate, Date dueDate, String message) {
		todos.add(new TodoItem(TodoItem.Status.TODO, priority, startDate, dueDate, message, Frequency.NONE));
		System.out.printf("added to %s: \"%s\"\n", fileName, message);
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void add(TodoItem addedItem) {
		todos.add(addedItem);
		System.out.printf("added to %s: \"%s\"\n", fileName, addedItem.getContents());
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void printFile() {
		for (int i = 1; i < todos.size() + 1; i++) {
			printLine(i);
		}
	}

	/**
	 * @@author A0149108E
	 */
	public TodoItem getItem(int i) {
		return this.todos.get(i);
	}

	/**
	 * @@author A0149108E
	 */
	private void printLine(int location) {
		System.out.printf("%d. %s\n", location, todos.get(location - 1));
	}

	/**
	 * @@author A0149108E
	 */
	public boolean isEmpty() {
		return this.todos.isEmpty();
	}

	/**
	 * @@author A0149108E
	 */
	public void write() {
		try {
			this.writer = new PrintWriter(new FileWriter(fileName, false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < todos.size(); i++) {
			writer.println(todos.get(i));
		}
		writer.flush();
		writer.close();
	}

	/**
	 * @@author A0149108E
	 */
	public void clear() {
		todos.clear();
		write();
	}

	/**
	 * @@author A0149108E
	 */
	public void exit() {
		write();
		writer.close();
	}

	/**
	 * @@author A0149108E
	 */
	public void sort() {
		Collections.sort(todos, TodoItem.getDueDateComparator());
		write();
	}

	/**
	 * @@author A0149108E
	 */
	public void sortByDueDate() {
		Collections.sort(todos, TodoItem.getDueDateComparator());
		write();
	}

	/**
	 * @@author A0149108E
	 */
	public void sortByStartDate() {
		Collections.sort(todos, TodoItem.getStartDateComparator());
		write();
	}

	/**
	 * @@author A0149108E
	 */
	public void sortByPriority() {
		Collections.sort(todos, TodoItem.getPriorityComparator());
		write();
	}

	/**
	 * @@author A0149108E
	 */
	public void sortByStatus() {
		Collections.sort(todos, TodoItem.getStatusComparator().reversed());
		write();
	}

	/**
	 * @@author A0149108E
	 */
	public void sortByContents() {
		Collections.sort(todos, TodoItem.getContentsComparator());
		write();
	}

	/**
	 * returns a list of todo items with due dates between begin and end
	 * @@author A0149108E
	 */
	public List<TodoItem> filterDueDates(LocalDateTime begin, LocalDateTime end) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		for (TodoItem todo : todos) {
			if (todo.getDueDate() == null)
				continue;
			Instant i = Instant.ofEpochMilli(todo.getDueDate().getTime());
			LocalDateTime comp = LocalDateTime.ofInstant(i, ZoneOffset.UTC);
			if ((begin.isBefore(comp) || begin.isEqual(comp)) && (end.isAfter(comp) || end.isEqual(comp))) {
				ret.add(todo);
			}
		}
		return ret;
	}

	/**
	 * returns a list of todo items with start dates between begin and end
	 * @@author A0149108E
	 */
	public List<TodoItem> filterStartDates(LocalDateTime begin, LocalDateTime end) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		for (TodoItem todo : todos) {
			// TODO the type stored in the TodoItem class to LocalDateTime
			// TODO remove jada, as java8.time is supersedes it.
			if (todo.getStartDate() == null)
				continue;
			Instant i = Instant.ofEpochMilli(todo.getStartDate().getTime());
			LocalDateTime comp = LocalDateTime.ofInstant(i, ZoneOffset.UTC);
			if ((begin.isBefore(comp) || begin.isEqual(comp)) && (end.isAfter(comp) || end.isEqual(comp))) {
				ret.add(todo);
			}
		}
		return ret;
	}

	/**
	 * @@author A0149108E
	 */
	public String toString() {
		StringBuilder ret = new StringBuilder();
		for (TodoItem item : todos) {
			ret.append(item.toString());
			ret.append("\n");
		}
		return ret.toString();
	}

	/**
	 * @@author A0135747X
	 */
	public void markDone(TodoItem tdi) {
		tdi.markDone();
		if (tdi.getFreq() != TodoItem.Frequency.NONE) {
			TodoItem replacement = new TodoItem(TodoItem.Status.TODO, tdi.getPriority(), tdi.getStartDate(),
					tdi.getDueDate(), tdi.getContents(), Frequency.NONE);
			switch (tdi.getFreq()) {
			case DAILY:
				if (tdi.getStartDate() != null) {
					tdi.getStartDate().setDate(tdi.getStartDate().getDate() + 1);
				}
				tdi.getDueDate().setDate(tdi.getDueDate().getDate() + 1);
				break;
			case WEEKLY:
				if (tdi.getStartDate() != null) {
					tdi.getStartDate().setDate(tdi.getStartDate().getDate() + 7);
				}
				tdi.getDueDate().setDate(tdi.getDueDate().getDate() + 7);
				break;
			case MONTHLY:
				if (tdi.getStartDate() != null) {
					tdi.getStartDate().setMonth(tdi.getStartDate().getMonth() + 1);
				}
				tdi.getDueDate().setMonth(tdi.getDueDate().getMonth() + 1);
				break;
			case YEARLY:
				if (tdi.getStartDate() != null) {
					tdi.getStartDate().setYear(tdi.getStartDate().getYear() + 1);
				}
				tdi.getDueDate().setYear(tdi.getDueDate().getYear() + 1);
				break;
			}
			todos.add(replacement);
		}
	}

	/**
	 * @@author A0135747X
	 */
	public void markUndone(TodoItem tdi) {
		tdi.markUndone();
	}

	/**
	 * @@author A0135747X
	 */
	public void toggle(TodoItem tdi) {
		if (tdi.getStatus() == Status.DONE) {
			tdi.markUndone();
		} else {
			tdi.markDone();
		}
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void updateEndDate(TodoItem tdi, Date newDate) {
		tdi.setDueDate(newDate);
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void updateStartDate(TodoItem tdi, Date newDate) {
		tdi.setStartDate(newDate);
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void updatePriority(TodoItem tdi, int newPriority) {
		tdi.setPriority(newPriority);
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void updateFrequency(TodoItem tdi, Frequency newFreq) {
		tdi.setFreq(newFreq);
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void updateContents(TodoItem tdi, String newContents) {
		tdi.setContents(newContents);
		write();
	}

	/**
	 * @@author A0135747X
	 */
	public void updateRecur() {
		Date now = new Date();
		ArrayList<TodoItem> todosToAdd = new ArrayList<TodoItem>();
		for (TodoItem tdi : todos) {
			TodoItem replacement;
			switch (tdi.getFreq()) {
			case DAILY:
				updateDailyRecur(now, tdi, todosToAdd);
				break;
			case WEEKLY:
				updateWeeklyRecur(now, tdi, todosToAdd);
				break;
			case MONTHLY:
				updateMonthlyRecur(now, tdi, todosToAdd);
				break;
			case YEARLY:
				updateYearlyRecur(now, tdi, todosToAdd);
				break;
			default:
				break;
			}
		}
		todos.addAll(todosToAdd);
	}

	/**
	 * @@author A0135747X
	 */
	private void updateYearlyRecur(Date now, TodoItem tdi, ArrayList<TodoItem> todosToAdd) {
		TodoItem replacement;
		while (tdi.getDueDate().before(now)) {
			replacement = createReplacement(tdi);
			todosToAdd.add(replacement);
			if (tdi.getStartDate() != null) {
				tdi.getStartDate().setYear(tdi.getStartDate().getYear() + 1);
			}
			tdi.getDueDate().setYear(tdi.getDueDate().getYear() + 1);
		}
	}

	/**
	 * @@author A0135747X
	 */
	private void updateMonthlyRecur(Date now, TodoItem tdi, ArrayList<TodoItem> todosToAdd) {
		TodoItem replacement;
		while (tdi.getDueDate().before(now)) {
			replacement = createReplacement(tdi);
			todosToAdd.add(replacement);
			if (tdi.getStartDate() != null) {
				tdi.getStartDate().setMonth(tdi.getStartDate().getMonth() + 1);
			}
			tdi.getDueDate().setMonth(tdi.getDueDate().getMonth() + 1);
		}
	}

	/**
	 * @@author A0135747X
	 */
	private void updateWeeklyRecur(Date now, TodoItem tdi, ArrayList<TodoItem> todosToAdd) {
		TodoItem replacement;
		while (tdi.getDueDate().before(now)) {
			replacement = createReplacement(tdi);
			todosToAdd.add(replacement);
			if (tdi.getStartDate() != null) {
				tdi.getStartDate().setDate(tdi.getStartDate().getDate() + 7);
			}
			tdi.getDueDate().setDate(tdi.getDueDate().getDate() + 7);
		}
	}

	/**
	 * @@author A0135747X
	 */
	private void updateDailyRecur(Date now, TodoItem tdi, ArrayList<TodoItem> todosToAdd) {
		TodoItem replacement;
		while (tdi.getDueDate().before(now)) {
			replacement = createReplacement(tdi);
			todosToAdd.add(replacement);
			if (tdi.getStartDate() != null) {
				tdi.getStartDate().setDate(tdi.getStartDate().getDate() + 1);
			}
			tdi.getDueDate().setDate(tdi.getDueDate().getDate() + 1);
		}
	}

	/**
	 * @@author A0135747X
	 */
	private TodoItem createReplacement(TodoItem tdi) {
		TodoItem replacement;
		replacement = new TodoItem(tdi.getStatus(), tdi.getPriority(),
				new Date(tdi.getStartDate().getTime()),
				new Date(tdi.getDueDate().getTime()),
				tdi.getContents(), Frequency.NONE);
		return replacement;
	}

	/**
	 * @@author A0135747X
	 */
	public void powerSearchString(String toFind) {
		PowerSearcher ps = new PowerSearcher();
		ps.powerSearchString(todos, toFind);
	}

	/**
	 * @@author A0135747X
	 */
	public void findFreeTime() {
		PowerSearcher ps = new PowerSearcher();
		ps.findFreeTime(todos);
	}

	/**
	 * @@author A0135747X
	 */
	public void findOverlap() {
		PowerSearcher ps = new PowerSearcher();
		ps.findOverlap(todos);
	}

	/**
	 * @@author A0135747X
	 */
	public void searchPriority(int p) {
		PowerSearcher ps = new PowerSearcher();
		ps.prioritySearch(todos, p);
	}

	/**
	 * @@author A0135747X
	 */
	public void searchInTimeBlock(Date timeBlockStart, Date timeBlockEnd) {
		TodoDateSearcher tds = new TodoDateSearcher();
		tds.searchTimeBlock(todos, timeBlockStart, timeBlockEnd);
	}

	/**
	 * @@author A0149108E
	 */
	public List<TodoItem> filterFloatingTodos() {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		for (TodoItem todo : todos) {
			if (todo.getStartDate() == null && todo.getDueDate() == null) {
				ret.add(todo);
			}
		}
		return ret;
	}

	/**
	 * @@author A0149108E
	 */
	public String getFileName() {
		return this.fileName;
	}
}
