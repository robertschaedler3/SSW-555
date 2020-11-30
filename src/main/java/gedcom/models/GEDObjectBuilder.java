package gedcom.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class GEDObjectBuilder {

    private static final String DATE_FORMAT = "dd MMM yyy";
    private static SimpleDateFormat dateFmt = new SimpleDateFormat(DATE_FORMAT);

    public Date buildDate(int date, int month, int year) {
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

    public Date buildDate(String date) {
        try {
            return dateFmt.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

}
