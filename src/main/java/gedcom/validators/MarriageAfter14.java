package gedcom.validators;

import gedcom.Validator;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import java.util.Calendar;

public class MarriageAfter14 extends Validator {

    public MarriageAfter14(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        for (Family family : gedFile.getFamilies()) {

            Calendar marriage = Calendar.getInstance();
            marriage.setTime(family.getMarriage());

            Individual husband = family.getHusband();
            Calendar husbandMinMarriage = Calendar.getInstance();
            husbandMinMarriage.setTime(husband.getBirthday());
            husbandMinMarriage.add(Calendar.YEAR, 14);

            Individual wife = family.getWife();
            Calendar wifeMinMarriage = Calendar.getInstance();
            wifeMinMarriage.setTime(wife.getBirthday());
            wifeMinMarriage.add(Calendar.YEAR, 14);

            if (marriage.compareTo(husbandMinMarriage) < 0) {
                System.out.printf("Anomaly US10: %s (%s) was married below the age of 14\n", husband.getName(), husband.getID());
                valid = false;
            }

            if (marriage.compareTo(wifeMinMarriage) < 0) {
                System.out.printf("Anomaly US10: %s (%s) was married below the age of 14\n",  wife.getName(), wife.getID());
                valid = false;
            }
        }
        return valid;
    }
}
