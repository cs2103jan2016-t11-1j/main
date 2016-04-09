package model;

import java.util.Comparator;
import java.util.Date;

public class TimelineNode implements Comparator<TimelineNode> {
	public enum DateType {
		START, END
	};

	private String content;
	private Date date;
	private DateType dateType;

	public TimelineNode(String content, Date date) {
		this.content = content;
		this.date = date;
		this.dateType = DateType.END;
	}

	public TimelineNode(Date date, String content) {
		this.content = content;
		this.date = date;
		this.dateType = DateType.START;
	}

	public String getContent() {
		return this.content;
	}

	public Date getDate() {
		return this.date;
	}

	public DateType getDateType() {
		return this.dateType;
	}

	@Override
	public int compare(TimelineNode tln1, TimelineNode tln2) {
		int cmp = tln1.date.compareTo(tln2.date);
		if (cmp == 0) {
			return tln1.content.compareTo(tln2.content);
		} else {
			return cmp;
		}
	}

}
