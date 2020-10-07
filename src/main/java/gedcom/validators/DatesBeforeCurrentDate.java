package gedcom.validators;

import gedcom.Validator;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import java.util.Date;

public class DatesBeforeCurrentDate extends Validator {

    public DatesBeforeCurrentDate(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;
        Date now = new Date(System.currentTimeMillis());

        for (Individual individual : gedFile.getIndividuals()) {
            if (individual.getBirthday().after(now)) {
                System.out.printf("Error US01: Birthday of %s (individual %s) occurs after the current date\n", individual.getName(), individual.getID());
                valid = false;
            }
            if (individual.getDeath() != null) {
                if (individual.getDeath().after(now)) {
                    System.out.printf("Error US01: Death of %s (individual %s) occurs after the current date\n", individual.getName(),individual.getID());
                    valid = false;
                }
            }
        }

        for (Family family : gedFile.getFamilies()) {
            if (family.getMarriage().after(now)) {
                System.out.printf("Error US01: Marriage of family %s occurs after the current date\n", family.getID());
                valid = false;
            }
            if (family.getDivorce() != null) {
                if (family.getDivorce().after(now)) {
                    System.out.printf("Error US01: Divorce of family %s occurs after the current date\n", family.getID());
                    valid = false;
                }
            }
        }

        return valid;
    }
}
