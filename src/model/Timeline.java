package model;

import java.util.ArrayList;

/**
 * 
 * @author A0135747X
 *
 */
public class Timeline {
	private ArrayList<TimelineNode> timeline;

	public Timeline() {
		timeline = new ArrayList<TimelineNode>();
	}

	public void add(TimelineNode tln) {
		timeline.add(tln);
		timeline.sort(tln);
	}

	public ArrayList<TimelineNode> getTimeline() {
		return timeline;
	}
}
