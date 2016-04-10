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
	public void findFreeTime(List<TodoItem> todos) {
		Timeline events = new Timeline();
		for (TodoItem tdi : todos) {
			if (tdi.getDueDate() != null && tdi.getStartDate() != null) {
				events.add(new TimelineNode(tdi.getStartDate(), tdi.getContents()));
				events.add(new TimelineNode(tdi.getContents(), tdi.getDueDate()));
				// System.out.println((todos.indexOf(tdi)+1) + ". " + tdi + "
				// has been added to the timeline");
			}
		}
		Date freeTimeEnd = new Date();
		ArrayList<TimelineNode> currOverlap = new ArrayList<TimelineNode>();
		ArrayList<Date> freeTimeSlots = new ArrayList<Date>();
		for (TimelineNode event : events.getTimeline()) {
			if (event.getDateType() == DateType.START) {
				if (currOverlap.size() == 0) {
					freeTimeEnd = event.getDate();
				}
				currOverlap.add(event);
			} else {
				if (currOverlap.size() == 1) {
					// System.out.println("Free Time found before " +
					// freeTimeEnd + " and after " + event.getDate());
					if (freeTimeSlots.contains(freeTimeEnd)) {
						freeTimeSlots.remove(freeTimeEnd);
						freeTimeSlots.add(event.getDate());
					} else {
						freeTimeSlots.add(freeTimeEnd);
						freeTimeSlots.add(event.getDate());
					}
				}
				for (TimelineNode tln : currOverlap) {
					if (tln.getContent().equals(event.getContent())) {
						currOverlap.remove(tln);
						break;
					}
				}
			}
		}
		for (int i = 0; i < freeTimeSlots.size(); i++) {
			if (i % 2 == 0) {
				System.out.println("Free Time found before: " + freeTimeSlots.get(i));
			} else {
				System.out.println("Free Time found after: " + freeTimeSlots.get(i));
			}
		}
	}

	/*
	 * Find overlapping tasks
	 */

	public void findOverlap(List<TodoItem> todos) {
		Timeline events = new Timeline();
		for (TodoItem tdi : todos) {
			if (tdi.getDueDate() != null && tdi.getStartDate() != null) {
				events.add(new TimelineNode(tdi.getStartDate(), tdi.getContents()));
				events.add(new TimelineNode(tdi.getContents(), tdi.getDueDate()));
			}
		}
		ArrayList<TimelineNode> currOverlap = new ArrayList<TimelineNode>();
		Date overlapStart = new Date(0);
		for (TimelineNode event : events.getTimeline()) {
			if (event.getDateType() == DateType.START) {
				if (currOverlap.size() == 1) {
					overlapStart = event.getDate();
				}
				currOverlap.add(event);
			} else {
				if (currOverlap.size() == 2) {
					System.out.println("Overlap found from " + overlapStart + " to " + event.getDate());
				}
				for (TimelineNode tln : currOverlap) {
					if (tln.getContent().equals(event.getContent())) {
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
		toFind = toFind.toLowerCase();
		ArrayList<String> permutations = new ArrayList<String>();
		String[] permutationComponents = toFind.split(" ");
		ArrayList<String> toPermutate = new ArrayList<String>();
		for (String s : permutationComponents) {
			toPermutate.add(s);
		}
		List<String> permutated = permutate(toPermutate);
		Collections.sort(permutated, CustomStringComparator());
		int[] hits = new int[todos.size()];
		for (int i=0; i<todos.size(); i++){
			String[] words = todos.get(i).getContents().split(" ");
			for (String s: permutated){
				for (String word: words){
					int hitCounter;
					if (s.length()<=3 || word.length()<=3){
						hitCounter = 3 - LevenshteinDist(s, word);
						/*
						if (s.length()>word.length()){
							hitCounter = word.length();
						}else{
							hitCounter = s.length();
						}
						*/
					}else{
						hitCounter = word.length() - LevenshteinDist(s, word);
						/*
						if (s.length()>word.length()){
							hitCounter = s.length() - (s.length()-word.length());
						}else{
							hitCounter = word.length() - (word.length()-s.length());
						}
						*/
					}
					hitCounter = hitCounter - LevenshteinDist(s,word);
					if (hitCounter>0){
						hits[i] += hitCounter;
					}
				}
			}
		}
		for (int i = 0; i < todos.size(); i++) {
			for (String s : permutated) {
				if (todos.get(i).getContents().toLowerCase().contains(s.toLowerCase())) {
					hits[i] = hits[i] + 1;
				}
			}
		}
		int mostHits = 0;
		for (int i : hits) {
			if (i > mostHits) {
				mostHits = i;
			}
		}
		if (mostHits>0){
			System.out.printf("User Power Search Results for %s:\n", toFind);
			for (int i=mostHits; i>0; i--){
				if (i==mostHits){
					System.out.println("Most relevant:");
				}else if (i==(mostHits/3*2)){
					System.out.println("Moderately relevant:");
				}else if(i==(mostHits/3)){
					System.out.println("Least relevant:");
				}
				for (int j=0; j<hits.length; j++){
					if (hits[j]==i){
						System.out.println(j + ". " + todos.get(j));
					}
				}
			}
		} else {
			System.out.println(toFind + " was not found.");
		}
	}

	private List<String> permutate(List<String> toPermutate) {
		List<String> permutated = new ArrayList<String>();
		if (toPermutate.size() == 1 || toPermutate.size() == 0) {
			return toPermutate;
		} else {
			String current = toPermutate.get(0);
			List<String> remainingPermutations = permutate(toPermutate.subList(1, toPermutate.size()));
			permutated.addAll(remainingPermutations);
			for (String s : remainingPermutations) {
				permutated.add(current + " " + s);
			}
			permutated.add(current);
			return permutated;
		}
	}

	private Comparator<String> CustomStringComparator() {
		return new Comparator<String>() {
			public int compare(String i, String j) {
				int cmp = Integer.compare(i.length(), j.length());
				if (cmp == 0) {
					return i.compareTo(j);
				} else {
					return -(cmp);
				}
			}
		};
	}

	/*
	 * Search by priority
	 */

	public void prioritySearch(List<TodoItem> todos, int p) {
		for (int i = 0; i < todos.size(); i++) {
			if (todos.get(i).getPriority() == p) {
				System.out.println("Priority " + p + " found in line " + (i + 1) + ". " + todos.get(i).getContents());
			}
		}
	}
	
	/**
	 * The method compares 2 strings and returns the Levenshtein (edit) distance between them.
	 * the Levenshtein distance between two words is the minimum number of single-character edits
	 * (i.e. insertions, deletions or substitutions) required to change one word into the other.
	 * 
	 * @param s1
	 * @param s2
	 * @returns the integer representing the Levenshtein (edit) distance between s1 and s2
	 */
	
	public int LevenshteinDist(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		// i == 0
		int [] costs = new int [s2.length() + 1];
		for (int j = 0; j < costs.length; j++){
			costs[j] = j;
		}
		for (int i = 1; i <= s1.length(); i++) {
			// j == 0; nw = lev(i - 1, j)
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= s2.length(); j++){
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), s1.charAt(i - 1) == s2.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[s2.length()];
	}
}
