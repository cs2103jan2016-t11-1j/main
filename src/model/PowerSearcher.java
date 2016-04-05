package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import model.TimelineNode.DateType;

public class PowerSearcher {
	private final static Logger LOGGER = Logger.getLogger(TodoDateSearcher.class.getName());
	
	public void findFreeTime(List<TodoItem> todos){
		Timeline events = new Timeline();
		for (TodoItem tdi: todos){
			if (tdi.getDueDate() != null && tdi.getStartDate() != null){
				events.add(new TimelineNode(tdi.getStartDate(),tdi.getContents()));
				events.add(new TimelineNode(tdi.getContents(),tdi.getDueDate()));
			}
		}
		int counter = 0;
		Date[] timeSlot = new Date[2];
		System.out.print("Free time found ");
		for (TimelineNode eventDate: events.getTimeline()){
			if (eventDate.getDateType()==DateType.START){
				if (counter == 0){
					counter++;
					timeSlot[0] = eventDate.getDate();
				}else{
					counter++;
				}
			}else{
				if (counter != 0){
					counter--;
				}else{
					counter--;
					timeSlot[1] = eventDate.getDate();
				}
			}
			if (counter == 0 && timeSlot[0] != null && timeSlot[1] != null){
				System.out.println("before " + timeSlot[0].getTime() + " after " + timeSlot[1].getTime());
			}
		}
	}
}
