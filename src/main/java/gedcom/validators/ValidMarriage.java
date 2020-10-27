package gedcom.validators;

import java.util.Calendar;
import java.util.Date;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class ValidMarriage extends Validator {

    private boolean valid = true;

    public ValidMarriage(Validator validator) {
        super(validator);
    }

    private void log(String message, Object... args) {
        String fullMessage = String.format("%s\n", message);
        System.out.printf(fullMessage, args);
        valid = false;
    }

    private Date addYears(Date d, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    private void checkMinMarriageAge(Individual individual, Family family) {
        Date birth = individual.getBirthday();
        Date marriage = family.getMarriage();
        if (marriage != null && birth != null) {
            Date minMarriage = addYears(birth, Family.MIN_MARRIAGE_AGE);
            if (marriage.compareTo(minMarriage) < 0) {
                log("Anomaly US10: %s was married below the age of 14 in family %s.\n", individual, family);
            }
        }
    }

    private void checkMarriageAfterBirth(Individual individual, Family family) {
        Date birth = individual.getBirthday();
        Date marriage = family.getMarriage();

        if (marriage != null && birth != null && marriage.before(birth)) {
            log("Error US02: Birth of %s occurs after of marriage in family %s.", individual, family);
        }
    }

    private void checkMarriageBeforeDeath(Individual individual, Family family) {
        Date death = individual.getDeath();
        Date marriage = family.getMarriage();

        if (marriage != null && death != null && marriage.after(death)) {
            log("Error US05: Death of %s occurs before of marriage in family %s.", individual, family);
        }
    }

    protected boolean check(GEDFile gedFile) {

        for (Family family : gedFile.getFamilies()) {

            Individual husband = family.getHusband();
            Individual wife = family.getWife();

            if (husband != null) {
                checkMinMarriageAge(husband, family);
                checkMarriageAfterBirth(husband, family);
                checkMarriageBeforeDeath(husband, family);
            }
            
            if (wife != null) {
                checkMinMarriageAge(wife, family);
                checkMarriageAfterBirth(wife, family);
                checkMarriageBeforeDeath(wife, family);
            }
        }

        return valid;
    }

}
