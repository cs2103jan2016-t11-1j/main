package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class PowerSearcher {
	private final static Logger LOGGER = Logger.getLogger(TodoDateSearcher.class.getName());
	
	public ArrayList<Date[]> findFreeTime(List<TodoItem> todos){
		List<TodoItem> events = new ArrayList<TodoItem>();
		for (TodoItem tdi: todos){
			if (tdi.getDueDate() != null && tdi.getStartDate() != null){
				events.add(tdi);
			}
		}
		Collections.sort(events, TodoItem.getStartDateComparator());
		Date currentFreeTimeStart = new Date();
		Date currentFreeTimeEnd = currentFreeTimeStart;
		ArrayList<Date[]> freeTimeSlots = new ArrayList<Date[]>();
		for (TodoItem event: events){
			Date[] freeTimeSlot = new Date[2];
			if (event.getStartDate().before(currentFreeTimeEnd)){
				currentFreeTimeEnd = event.getStartDate();
			}
			if (currentFreeTimeStart.before(currentFreeTimeEnd)){
				freeTimeSlot[0] = currentFreeTimeStart;
				freeTimeSlot[1] = currentFreeTimeEnd;
				freeTimeSlots.add(freeTimeSlot);
			}
			if (event.getDueDate().after(currentFreeTimeStart)){
				currentFreeTimeStart = event.getDueDate();
			}
		}
		return freeTimeSlots;
	}
}
