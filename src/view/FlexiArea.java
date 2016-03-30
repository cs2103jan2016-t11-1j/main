package view;
import javafx.scene.control.TextArea;
import model.TodoFile;
//import java.time;

//TODO make this just a box, not a text area
public class FlexiArea extends TextArea {
	private TodoFile info;
	private Mode mode;
	private TimeState timeState;
	public enum Mode {
		/*
		 * SORT_DATE: Show the due dates
		 * SORT_PRIORITY: Sort by priority
		 * SORT_STATUS: Sort by status
		 * SORT_CONTENTS: Sort by contents
		 * HEAT_MAP: show the heat map
		 */
		SORT_DATE, SORT_PRIORITY, SORT_STATUS, SORT_CONTENTS, HEAT_MAP
	};
	public enum TimeState {
		/* This is the chunk of time shown.
		 * DAY: view each day
		 * WEEK: view each week
		 * MONTH: view each month
		 * ALL: everything, in a big bag
		 * FUTURE: everything in the future, in a big bag
		 */
		DAY, WEEK, MONTH, ALL, FUTURE
	}
	public FlexiArea (TodoFile info) {
		this.info = info;
		setMode(Mode.SORT_CONTENTS);
		setTimeState(TimeState.WEEK);
	}

	public void setTimeState(TimeState timeState) {
		this.timeState = timeState;
		//TODO make it actually affect the view, should just append new view to children.
	}
	public void setMode(Mode newState) {
		switch (newState) {
		case SORT_PRIORITY:
			break;
		case SORT_CONTENTS:
			break;
		case SORT_DATE:
			break;
		case SORT_STATUS:
			break;
		case HEAT_MAP:
			break;
		}
		this.mode = newState;
		//TODO make it actually affect the view, should just append new view to children.
	}
	public Mode getMode() {
		return mode;
	}	
	public TimeState getTimeState() {
		return timeState;
	}
}