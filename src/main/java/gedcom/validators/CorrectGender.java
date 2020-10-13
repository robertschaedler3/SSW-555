package gedcom.validators;

import gedcom.models.Family;
import gedcom.models.Individual;
import gedcom.interfaces.Gender;
import gedcom.models.GEDFile;

public class CorrectGender extends Validator {

    public CorrectGender(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        for (Family family : gedFile.getFamilies()) {
            Individual husband = family.getHusband();
            if (husband != null && husband.getGender() != Gender.M) {
                System.out.printf("Error US21: Husband %s (individual %s) gender is not M\n", husband.getName(), husband.getID());
                valid = false;
            }

            Individual wife = family.getWife();
            if (wife != null && wife.getGender() != Gender.F) {
                System.out.printf("Error US21: Wife %s (individual %s) gender is not F\n", wife.getName(), wife.getID());
                valid = false;
            }

        }

        return valid;
    }
    
}