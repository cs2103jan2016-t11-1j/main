package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class TodoTimeSearcher {
	private final static Logger LOGGER = Logger.getLogger(TodoTimeSearcher.class.getName());

	public List<TodoItem> searchTime(List<TodoItem> todos, Date toFind) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				Date currDateTime = tdi.getDueDate();
				boolean sameHour = currDateTime.getHours() == toFind.getHours();
				boolean sameMinute = currDateTime.getMinutes() == toFind.getMinutes();
				boolean sameSecond = currDateTime.getSeconds() == toFind.getSeconds();
				if (sameHour && sameMinute && sameSecond) { // same time
					assert (sameHour);
					assert (sameMinute);
					assert (sameSecond);
					ret.add(tdi);
					System.out.println(currDateTime.getHours() + ":" + currDateTime.getMinutes() + ":"
							+ currDateTime.getSeconds() + " found in line " + i + ". " + tdi.getContents());
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by Time");
		}
		return ret;
	}

	public List<TodoItem> searchHour(List<TodoItem> todos, Date toFind) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				Date currDateTime = tdi.getDueDate();
				boolean sameHour = currDateTime.getHours() == toFind.getHours();
				if (sameHour) {// same hour
					assert (sameHour);
					System.out.println(currDateTime.getHours() + ":" + currDateTime.getMinutes() + ":"
							+ currDateTime.getSeconds() + " found in line " + i + ". " + tdi.getContents());
					ret.add(tdi);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by Time");
		}
		return ret;
	}

	public List<TodoItem> searchAfterTime(List<TodoItem> todos, Date toFind) {
		List<TodoItem> ret = new ArrayList<TodoItem>();
		try {
			int i = 1;
			for (TodoItem tdi : todos) {
				Date currDateTime = tdi.getDueDate();
				boolean sameHour = currDateTime.getHours() == toFind.getHours();
				boolean sameMinute = currDateTime.getMinutes() == toFind.getMinutes();
				boolean sameSecond = currDateTime.getSeconds() == toFind.getSeconds();
				if (sameHour && sameMinute && sameSecond) { // same time
					assert (sameHour);
					assert (sameMinute);
					assert (sameSecond);
					System.out.println(currDateTime.getHours() + ":" + currDateTime.getMinutes() + ":"
							+ currDateTime.getSeconds() + " found in line " + i + ". " + tdi.getContents());
					ret.add(tdi);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe("Error searching by Time");
		}
		return ret;
	}
}
