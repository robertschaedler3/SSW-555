package gedcom.validators;

import gedcom.models.GEDFile;
import gedcom.models.Family;
import gedcom.models.Individual;

public class MarriageAfterBirthBeforeDeath extends Validator {
    /**
     * This checks if marriage is after birth and marriage before death.
     * @param validator
     */
    public MarriageAfterBirthBeforeDeath(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        for (Family family : gedFile.getFamilies()) {

            Individual husband = family.getHusband();
            Individual wife = family.getWife();
            
            //First: Is marriage after birth?
            if ((family.getMarriage().before(husband.getBirthday())) || (family.getMarriage().before(wife.getBirthday()))) {
                System.out.printf("Birth of parent occurs after of marriage in family %s", family.getID());
                valid = false;
            }

            //Second: Is marriage before death?
            if ((husband.getDeath() != null && family.getMarriage().after(husband.getDeath()))
                    || (wife.getDeath() != null && family.getMarriage().after(wife.getDeath()))) {
                System.out.printf("Error US05: Death of parent occurs before of marriage in family %s\n", family.getID());
                valid = false;
                }
        }
        return valid;
    }
}
