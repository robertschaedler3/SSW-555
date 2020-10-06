package gedcom.validators;

import java.util.Map;

import gedcom.Validator;
import gedcom.models.GEDFile;
import gedcom.models.Family;

public class MarriageBeforeDivorce extends Validator {

    public MarriageBeforeDivorce(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        for (Map.Entry<String, Family> entry : gedFile.getFamilies().entrySet()) {
            Family family = entry.getValue();
            if (family.getDivorce() != null && family.getMarriage().after(family.getDivorce())) {
                System.out.printf("Error US04: Divorce of family occurs before marriage in family: %s\n", family.getID());
                valid = false;
            }
        }
        return valid;
    }

}
