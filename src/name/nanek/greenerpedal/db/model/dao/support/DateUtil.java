package name.nanek.greenerpedal.db.model.dao.support;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Calendar getCalendarWithClearedTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Date getDateForMonthDayYear(Calendar cal, final int month, final int day, final int year) {
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.YEAR, year);       
        return cal.getTime();
    }

}
