package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import model.TimelineNode.DateType;

public class PowerSearcher {
	private final static Logger LOGGER = Logger.getLogger(TodoDateSearcher.class.getName());
	
	/*
	 * Free Time Power Search
	 */
	
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
				System.out.println("before " + timeSlot[0] + " after " + timeSlot[1]);
			}
		}
	}
	
	/*
	 * String Power Search
	 */
	
	public void powerSearchString(List<TodoItem> todos, String toFind) {
		ArrayList<String> permutations = new ArrayList<String>();
		String[] permutationComponents = toFind.split(" ");
		ArrayList<String> toPermutate = new ArrayList<String>();
		for (String s: permutationComponents){
			toPermutate.add(s);
		}
		List<String> permutated = permutate(toPermutate);
		Collections.sort(permutated, CustomStringComparator());
		TodoStringSearcher tds = new TodoStringSearcher();
		for (String s: permutated){
			tds.searchString(todos, s);
		}
	}

	private List<String> permutate(List<String> toPermutate){
		List<String> permutated = new ArrayList<String>();
		if (toPermutate.size()==1 || toPermutate.size()==0){
			return toPermutate;
		}else{
			String current = toPermutate.get(0);
			List<String> remainingPermutations = permutate(toPermutate.subList(1, toPermutate.size()));
			permutated.addAll(remainingPermutations);
			for (String s: remainingPermutations){
				permutated.add(current +" "+ s);
			}
			permutated.add(current);
			return permutated;
		}
	}
	
	private Comparator<String> CustomStringComparator() {
		return new Comparator<String>() {
			public int compare(String i, String j) {
				int cmp = Integer.compare(i.length(), j.length());
				if (cmp==0){
					return i.compareTo(j);
				}else{
					return -(cmp);
				}
			}
		};
	}
}
