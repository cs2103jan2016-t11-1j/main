package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * 
 * @author A0135747X
 *
 */
public class TodoStringSearcher {
	private final static Logger LOGGER = Logger.getLogger(TodoStringSearcher.class.getName());

	public List<TodoItem> searchString(List<TodoItem> todos, String toFind) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		try {
			boolean stringFound = false;
			int i = 1;
			for (TodoItem tdi : todos) {
				if (tdi.getContents().contains(toFind)) {
					ret.add(tdi);
					System.out.println(toFind + " found in line " + i + ". " + tdi.getContents());
					stringFound = true;
				}
				i++;
			}
			if (!stringFound) {
				System.out.println(toFind + " was not found in todos");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching String");
		}
		return ret;
	}

	public List<TodoItem> searchExactString(List<TodoItem> todos, String toFind) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				if (tdi.getContents().equals(toFind)) {
					System.out.println(toFind + " found in line " + i + ". " + tdi.getContents());
					ret.add(tdi);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching exact String");
		}
		return ret;
	}
}
