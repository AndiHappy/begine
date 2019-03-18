package begine.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat {
	
	public static final String TIME_FORMAT_H = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_FORMAT_D = "yyyyMMddHHmmss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String YEAR = "yyyy";
	public static final String MONTH = "MM";
	public static final String DAY = "dd";
	public static final String HOUR = "HH";
	public static final String MINUTE = "mm";
	public static final String SECOND = "ss";
	public static final String YYYYMMDD = "yyyyMMdd";

	public static String getFormatDate(Date date, String format) {
		String dateStr = null;
		try {
			if (date != null) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
				dateStr = simpleDateFormat.format(date);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dateStr;
	}

	public static Date convertDate(String dateStr, String format) {
		Date date = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			date = simpleDateFormat.parse(dateStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return date;
	}
}