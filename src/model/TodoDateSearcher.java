package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class TodoDateSearcher {
	private final static Logger LOGGER = Logger.getLogger(TodoDateSearcher.class.getName());
	private final String[] Month = { "January", "February", "March", "April", "May", "June", "July", "September",
			"October", "November", "December" };

	public List<TodoItem> searchDueDate(List<TodoItem> todos, Date toFind) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				if (tdi.getDueDate().equals(toFind)) {
					System.out.println("Due Date: " + toFind + "found in line " + i);
					ret.add(tdi);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by dueDate");
		}
		return ret;
	}

	public List<TodoItem> searchStartDate(List<TodoItem> todos, Date toFind) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				if (sameDate(toFind, tdi.getStartDate())) {
					System.out.println("Start Date: " + toFind + "found in line " + i);
					ret.add(tdi);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by startDate");
		}
		return ret;
	}

	public List<TodoItem> searchDate(List<TodoItem> todos, Date toFind) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		try {
			int i = 1;
			boolean notFound = true;
			for (TodoItem tdi : todos) {
				if (tdi.getStartDate() != null && tdi.getDueDate() != null) {
					if (sameDate(toFind, tdi.getStartDate()) && sameDate(toFind, tdi.getDueDate())) {
						System.out.println("Start Date and End Date: " + toFind + " found in line " + i);
						notFound = false;
					} else if (sameDate(toFind, tdi.getStartDate())) {
						System.out.println("Start Date: " + toFind + " found in line " + i);
						notFound = false;
					} else if (sameDate(toFind, tdi.getDueDate())) {
						System.out.println("Due Date: " + toFind + " found in line " + i);
						notFound = false;
					}
				} else if (tdi.getStartDate() != null) {
					if (sameDate(toFind, tdi.getStartDate())) {
						System.out.println("Start Date: " + toFind + "found in line " + i);
						notFound = false;
					}
				} else if (tdi.getDueDate() != null) {
					if (sameDate(toFind, tdi.getDueDate())) {
						System.out.println("Due Date: " + toFind + "found in line " + i);
						notFound = false;
					}
				}
			}
			if (notFound) {
				System.out.println("The date: " + toFind + " was not found.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by Date");
		}
		return ret;
	}

	private boolean sameDate(Date toFind, Date tdiDate) {
		boolean sameDate = toFind.getDate() == tdiDate.getDate();
		boolean sameMonth = toFind.getMonth() == tdiDate.getMonth();
		boolean sameYear = toFind.getYear() == tdiDate.getYear();
		return (sameDate && sameMonth && sameYear);
	}

	public List<TodoItem> searchByMonth(List<TodoItem> todos, Date toFind) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				boolean sameStartMonth = false;
				boolean sameDueMonth = false;
				if (tdi.getStartDate() != null) {
					sameStartMonth = tdi.getStartDate().getMonth() == toFind.getMonth();
				}
				if (tdi.getStartDate() != null) {
					sameDueMonth = tdi.getDueDate().getMonth() == toFind.getMonth();
				}
				if (sameStartMonth && sameDueMonth) {
					System.out.println("Start Date and Due Date are in " + Month[toFind.getMonth()] + " in line " + i);
					ret.add(tdi);
				} else if (sameStartMonth) {
					System.out.println("Start Date is in " + Month[toFind.getMonth()] + " in line " + i);
					ret.add(tdi);
				} else if (sameDueMonth) {
					System.out.println("Due Date is in " + Month[toFind.getMonth()] + " in line " + i);
					ret.add(tdi);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by Date");
		}
		return ret;
	}

	public List<TodoItem> searchByYear(List<TodoItem> todos, Date toFind) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				boolean sameStartYear = false;
				if (tdi.getStartDate() != null) {
					sameStartYear = tdi.getStartDate().getYear() == toFind.getYear();
				}
				boolean sameDueYear = false;
				if (tdi.getDueDate() != null) {
					sameDueYear = tdi.getDueDate().getYear() == toFind.getYear();
				}
				if (sameStartYear && sameDueYear) {
					System.out.println("Start Date and Due Date are in " + (toFind.getYear() + 1900) + " in line " + i);
					ret.add(tdi);
				} else if (sameStartYear) {
					System.out.println("Start Date is in " + (toFind.getYear() + 1900) + " in line " + i);
					ret.add(tdi);
				} else if (sameDueYear) {
					System.out.println("Due Date is in " + (toFind.getYear() + 1900) + " in line " + i);
					ret.add(tdi);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by Date");
		}
		return ret;
	}

	public List<TodoItem> searchTimeBlock(List<TodoItem> todos, Date blockStart, Date blockEnd) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		ArrayList<TodoItem> insideTimeBlock = new ArrayList<TodoItem>();
		for (int i = 0; i < todos.size(); i++) {
			TodoItem tdi = todos.get(i);
			boolean startInsideBlock = false;
			boolean dueInsideBlock = false;
			if (tdi.getStartDate() != null) {
				startInsideBlock = tdi.getStartDate().getTime() >= blockStart.getTime();
			}
			if (tdi.getDueDate() != null) {
				dueInsideBlock = tdi.getDueDate().getTime() >= blockStart.getTime()
						&& tdi.getDueDate().getTime() <= blockEnd.getTime();
			}
			if (startInsideBlock && dueInsideBlock) {
				insideTimeBlock.add(tdi);
			} else if (tdi.getStartDate() == null && dueInsideBlock) {
				insideTimeBlock.add(tdi);
			}
		}
		if (insideTimeBlock.size() == 0) {
			System.out.println("No todos inside time block");
		} else {
			System.out.println("Todos found inside time block " + blockStart + " - " + blockEnd + ": ");
			for (TodoItem tdi : insideTimeBlock) {
				System.out.println(todos.indexOf(tdi) + ". " + tdi);
				ret.add(tdi);
			}
		}
		return ret;
	}
}
