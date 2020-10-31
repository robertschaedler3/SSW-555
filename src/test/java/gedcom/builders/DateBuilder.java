package gedcom.builders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public abstract class DateBuilder {

    private static final String DATE_FORMAT = "dd MMM yyy";
    private static SimpleDateFormat dateFmt = new SimpleDateFormat(DATE_FORMAT);

    public static Date build(int date, int month, int year) {
        if (date < 0 || year < 0 || month < 0 || month > 11) {
            throw new IllegalArgumentException();
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, date);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date build(String date) {
        try {
            return dateFmt.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date earliest(Date d1, Date d2) {
        if (d1 == null && d2 == null) {
            throw new IllegalArgumentException();
        }
        return (d1 == null) ? d2 : (d2 == null) ? d1 : (d1.before(d2) ? d1 : d2);
    }

    public static Date latest(Date d1, Date d2) {
        if (d1 == null && d2 == null) {
            throw new IllegalArgumentException();
        }
        return (d1 == null) ? d2 : (d2 == null) ? d1 : (d1.after(d2) ? d1 : d2);
    }

    public static Date getRandomDateBetween(Date d1, Date d2) {
        return new Date(ThreadLocalRandom.current().nextLong(d1.getTime(), d2.getTime()));
    }

    private static Date incrementDate(Date d, int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(field, amount);
        return c.getTime();
    }

    public static Date newDateYearsAfter(Date d, int years) {
        return incrementDate(d, Calendar.YEAR, years);
    }

    public static Date newDateMonthsAfter(Date d, int months) {
        return incrementDate(d, Calendar.MONTH, months);
    }

    public static Date newDateDaysAfter(Date d, int days) {
        return incrementDate(d, Calendar.DATE, days);
    }

}
