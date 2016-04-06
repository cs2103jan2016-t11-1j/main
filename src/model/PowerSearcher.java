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
	 * Find overlapping tasks
	 */
	
	public void findOverlap(List<TodoItem> todos){
		Timeline events = new Timeline();
		for (TodoItem tdi: todos){
			if (tdi.getDueDate() != null && tdi.getStartDate() != null){
				events.add(new TimelineNode(tdi.getStartDate(),tdi.getContents()));
				events.add(new TimelineNode(tdi.getContents(),tdi.getDueDate()));
			}
		}
		ArrayList<TimelineNode> currOverlap = new ArrayList<TimelineNode>();
		Date overlapStart = new Date(0);
		for (TimelineNode event: events.getTimeline()){
			if (event.getDateType()==DateType.START){
				if (currOverlap.size()==1){
					overlapStart = event.getDate();
				}
				currOverlap.add(event);
			}else{
				if (currOverlap.size()==2){
					System.out.println("Overlap found from " + overlapStart + " to " + event.getDate());
				}
				for (TimelineNode tln: currOverlap){
					if (tln.getContent().equals(event.getContent())){
						currOverlap.remove(tln);
						break;
					}
				}	
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
		int[] hits = new int[todos.size()];
		for (int i=0; i<todos.size(); i++){
			for (String s: permutated){
				if (todos.get(i).getContents().contains(s)){
					hits[i] = hits[i] + 1;
				}
			}
		}
		int mostHits=0;
		for (int i: hits){
			if (i>mostHits){
				mostHits = i;
			}
		}
		if (mostHits>0){
			System.out.println("User Power Search Results:");
			for (int i=mostHits; i>0; i--){
				for (int j=0; j<hits.length; j++){
					if (hits[j]==i){
						System.out.println(j + ". " + todos.get(j));
					}
				}
			}
		}else{
			System.out.println(toFind + " was not found.");
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
	
	/*
	 * Search by priority
	 */
	
	public void prioritySearch(List<TodoItem> todos, int p){
		for (int i=0; i<todos.size(); i++){
			if (todos.get(i).getPriority()==p){
				System.out.println("Priority " + p + " found in line " + (i+1) + ". " + todos.get(i).getContents());
			}
		}
	}
}
