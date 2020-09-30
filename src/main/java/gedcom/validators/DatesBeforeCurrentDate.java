package gedcom.validators;

import gedcom.Validator;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import java.util.Date;
import java.util.Map;

public class DatesBeforeCurrentDate extends Validator {

    public DatesBeforeCurrentDate(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        Map<String, Individual> individuals = gedFile.getIndividuals();

        Date now = new Date(System.currentTimeMillis());

        for (Map.Entry<String, Individual> entry : individuals.entrySet()) {
            Individual individual = entry.getValue();
            if (individual.getBirthday().after(now)) {
                System.out.printf("Birthday of individual %s occurs after the current date\n", individual.getID());
                valid = false;
            }
            if (individual.getDeath() != null) {
                if (individual.getDeath().after(now)) {
                    System.out.printf("Death of individual %s occurs after the current date\n", individual.getID());
                    valid = false;
                }
            }
        }

        for (Map.Entry<String, Family> entry : gedFile.getFamilies().entrySet()) {
            Family family = entry.getValue();
            if (family.getMarriage().after(now)) {
                System.out.printf("Marriage of family %s occurs after the current date\n", family.getID());
                valid = false;
            }
            if (family.getDivorce() != null) {
                if (family.getDivorce().after(now)) {
                    System.out.printf("Divorce of family %s occurs after the current date\n", family.getID());
                    valid = false;
                }
            }
        }

        return valid;
    }
}
