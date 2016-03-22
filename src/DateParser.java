import java.util.*;
import com.joestelmach.natty.*;
import java.util.Date;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.joda.time.DateTime;
import com.joestelmach.natty.CalendarSource;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * Date parser that sets default time to 10 am
 * Parser takes in date only in American format
 * Eg. 12/21 is parsed as 21 December
 */

public class DateParser {
	protected Parser natty = null;
	
	protected DateParser(){
		BasicConfigurator.configure();
		natty = new Parser();
		
	}
	
	public Date parse(String input) {
		List<Date> dateList = parseAndGetDateList(input);
		if (dateList == null) {
			return null;
		} else {
			Date date1 = dateList.get(0);
			System.out.println(date1);
			return date1;
		}
	}
	
	public Date parse(String input, Date defaultTime) {
		List<Date> dateList = parseAndGetDateList(input, defaultTime);
		if (dateList == null) {
			return null;
		} else {
			return dateList.get(0);
		}
	}
	
	public List<Date> parseAndGetDateList(String input) {
		Date defaultDate = getDateTodayTenAm();
		return parseAndGetDateList(input, defaultDate);
	}

	private Date getDateTodayTenAm() {
		DateTime midnightToday = new DateTime().withTimeAtStartOfDay();
		DateTime tenAM = midnightToday.plusHours(10);
		Date defaultDate = tenAM.toDate();
		return defaultDate;
	}
	
	public List<Date> parseAndGetDateList(String input, Date defaultTime) {
		assert(input != null); // input should always be checked to be not null
		
		CalendarSource.setBaseDate(defaultTime);
		List<DateGroup> baseDateGroupList = natty.parse(input);
		if (baseDateGroupList.isEmpty()) {
			return null;
		}
		DateGroup baseDateGroup = baseDateGroupList.get(0);
		List<Date> dateList = baseDateGroup.getDates();
		return dateList;
	}
}
