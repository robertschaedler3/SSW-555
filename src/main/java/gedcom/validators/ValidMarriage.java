package gedcom.validators;

import java.util.Calendar;
import java.util.Date;

import gedcom.logging.Error;
import gedcom.logging.Level;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.GEDObject;
import gedcom.models.Individual;

public class ValidMarriage extends Validator {

    public ValidMarriage(Validator validator) {
        super(validator);
    }

    private void log(Level level, Error error, GEDObject... objs) {
        if (level == Level.ERROR) {
            LOGGER.error(error, objs);
        } else if (level == Level.ANOMALY) {
            LOGGER.anomaly(error, objs);
        }
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
                log(Level.ANOMALY, Error.MARRIED_BELOW_MIN_MARRIAGE_AGE, individual, family);
            }
        }
    }

    private void checkMarriageAfterBirth(Individual individual, Family family) {
        Date birth = individual.getBirthday();
        Date marriage = family.getMarriage();

        if (marriage != null && birth != null && marriage.before(birth)) {
            log(Level.ERROR, Error.MARRIAGE_BEFORE_BIRTH, individual, family);
        }
    }

    private void checkMarriageBeforeDeath(Individual individual, Family family) {
        Date death = individual.getDeath();
        Date marriage = family.getMarriage();

        if (marriage != null && death != null && marriage.after(death)) {
            log(Level.ERROR, Error.DEATH_BEFORE_MARRIAGE, individual, family);
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
