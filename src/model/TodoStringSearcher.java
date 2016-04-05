package model;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class TodoStringSearcher {
	private final static Logger LOGGER = Logger.getLogger(TodoStringSearcher.class.getName());

	public void searchString(List<TodoItem> todos, String toFind) {
		try {
			boolean stringFound = false;
			int i = 1;
			for (TodoItem tdi : todos) {
				if (tdi.getContents().contains(toFind)) {
					System.out.println(toFind + " found in line " + i + ". " + tdi.getContents());
					stringFound = true;
				}
				i++;
			}
			if (!stringFound){
				System.out.println(toFind + " was not found in todos");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching String");
		}
	}
	
	public void searchExactString(List<TodoItem> todos, String toFind) {
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				if (tdi.getContents().equals(toFind)) {
					System.out.println(toFind + " found in line " + i + ". " + tdi.getContents());
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching exact String");
		}
	}
}
