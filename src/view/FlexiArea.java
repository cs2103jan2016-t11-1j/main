package view;
import javafx.scene.control.TextArea;
import model.TodoFile;

public class FlexiArea extends TextArea {
	private TodoFile info;
	public enum FlexiState {
		/*
		 * SORT_DATE: Sort by date
		 * SORT_TIME: Sort by time
		 * SORT_PRIORITY: Sort by priority
		 * SORT_STATUS: Sort by status
		 * SORT_CONTENTS: Sort by contents
		 * HEAT_MAP: show the heat map
		 */
		SORT_DATE, SORT_TIME, SORT_PRIORITY, SORT_STATUS, SORT_CONTENTS
	};
	private FlexiState currentState;
	public FlexiArea (TodoFile info) {
		this.info = info;
		currentState = FlexiState.SORT_CONTENTS;
	}
	public FlexiState getState() {
		return currentState;
	}
	public void setState(FlexiState newState) {
		currentState = newState;
		//TODO make the place affect the changes
	}
	public void print(Object in) {
		this.appendText(in.toString());
	}
	public void println(Object in) {
		this.appendText(in.toString() + "\n");
	}
}