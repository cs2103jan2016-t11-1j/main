package model;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class TodoDateSearcher {
	private final static Logger LOGGER = Logger.getLogger(TodoDateSearcher.class.getName());
	private final String[] Month = {"January", "February", "March", "April",
			"May", "June", "July", "September", "October", "November", "December"};

	public void searchDueDate(List<TodoItem> todos, Date toFind) {
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				if (tdi.getDueDate().equals(toFind)) {
					System.out.println("Due Date: " + toFind + "found in line " + i);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by dueDate");
		}
	}
	
	public void searchStartDate(List<TodoItem> todos, Date toFind) {
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				if (tdi.getStartDate().equals(toFind)) {
					System.out.println("Start Date: " + toFind + "found in line " + i);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by startDate");
		}
	}

	public void searchDate(List<TodoItem> todos, Date toFind) {
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				if(tdi.getStartDate().equals(toFind) && tdi.getDueDate().equals(toFind)){
					System.out.println("Start Date and End Date: " + toFind + "found in line " + i);
				}else if (tdi.getStartDate().equals(toFind)) {
					System.out.println("Start Date: " + toFind + "found in line " + i);
				}else if (tdi.getDueDate().equals(toFind)) {
					System.out.println("Due Date: " + toFind + "found in line " + i);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by Date");
		}
	}
	
	public void searchByMonth(List<TodoItem> todos, Date toFind) {
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				boolean sameStartMonth = tdi.getStartDate().getMonth()==toFind.getMonth();
				boolean sameDueMonth = tdi.getDueDate().getMonth()==toFind.getMonth();
				if(sameStartMonth && sameDueMonth){
					System.out.println("Start Date and Due Date are in " + Month[toFind.getMonth()] + " in line " + i);
				}else if (sameStartMonth) {
					System.out.println("Start Date is in " + Month[toFind.getMonth()] + " in line " + i);
				}else if (sameDueMonth) {
					System.out.println("Due Date is in " + Month[toFind.getMonth()] + " in line " + i);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by Date");
		}
	}

	public void searchByYear(List<TodoItem> todos, Date toFind) {
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				boolean sameStartYear = tdi.getStartDate().getYear()==toFind.getYear();
				boolean sameDueYear = tdi.getDueDate().getYear()==toFind.getYear();
				if(sameStartYear && sameDueYear){
					System.out.println("Start Date and Due Date are in " + (toFind.getYear()+1900) + " in line " + i);
				}else if (sameStartYear) {
					System.out.println("Start Date is in " + (toFind.getYear()+1900) + " in line " + i);
				}else if (sameDueYear) {
					System.out.println("Due Date is in " + (toFind.getYear()+1900) + " in line " + i);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by Date");
		}
	}
}
