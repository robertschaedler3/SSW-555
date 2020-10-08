//package gedcom.validators;
package gedcom.validators;

import gedcom.models.GEDFile;
import gedcom.models.Family;
import gedcom.models.Individual;

public class MarriageAfterBirth extends Validator {
    public MarriageAfterBirth(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        for (Family family : gedFile.getFamilies()) {

            Individual husband = family.getHusband();
            Individual wife = family.getWife();

            if ((family.getMarriage().before(husband.getBirthday())) || (family.getMarriage().before(wife.getBirthday()))) {
                System.out.printf("Birth of parent occurs after of marriage in family %s", family.getID());
                valid = false;
            }
        }
        return valid;
    }
}
