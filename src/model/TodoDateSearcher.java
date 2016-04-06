package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class TodoDateSearcher {
	private final static Logger LOGGER = Logger.getLogger(TodoDateSearcher.class.getName());
	private final String[] Month = {"January", "February", "March", "April",
			"May", "June", "July", "September", "October", "November", "December"};

	public List<TodoItem> searchDueDate(List<TodoItem> todos, Date toFind) {
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
	
	public List<TodoItem> searchStartDate(List<TodoItem> todos, Date toFind) {
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

	public List<TodoItem> searchDate(List<TodoItem> todos, Date toFind) {
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				if (tdi.getStartDate().equals(toFind) && tdi.getStartDate().equals(toFind)) {
					System.out.println("Start Date and End Date: " + toFind + "found in line " + i);
				}else if(tdi.getStartDate().equals(toFind)){
					System.out.println("Start Date: " + toFind + "found in line " + i);	
				}
				else if (tdi.getDueDate().equals(toFind)) {
					System.out.println("Due Date: " + toFind + "found in line " + i);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by Date");
		}
	}
	
	public List<TodoItem> searchByMonth(List<TodoItem> todos, Date toFind) {
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

	public List<TodoItem> searchByYear(List<TodoItem> todos, Date toFind) {
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
	
	public List<TodoItem> searchTimeBlock(List<TodoItem> todos, Date blockStart, Date blockEnd){
		ArrayList<TodoItem> insideTimeBlock = new ArrayList<TodoItem>();
		for (int i=0; i<todos.size(); i++){
			TodoItem tdi = todos.get(i);
			boolean startInsideBlock = false;
			boolean dueInsideBlock = false;
			if (tdi.getStartDate() != null){
				startInsideBlock = tdi.getStartDate().getTime()>=blockStart.getTime();
			}
			if (tdi.getDueDate() != null){
				dueInsideBlock = tdi.getDueDate().getTime()<=blockEnd.getTime();
			}
			if (startInsideBlock && dueInsideBlock){
				insideTimeBlock.add(tdi);
			}else if (tdi.getStartDate()==null && dueInsideBlock){
				insideTimeBlock.add(tdi);
			}
		}
		if (insideTimeBlock.size()==0){
			System.out.println("No todos inside time block");
		}else{
			System.out.println("Todos found inside time block " + blockStart + " - " + blockEnd + ": ");
			for (TodoItem tdi: insideTimeBlock){
				System.out.println(todos.indexOf(tdi) + ". " + tdi);
			}
		}
	}
}
