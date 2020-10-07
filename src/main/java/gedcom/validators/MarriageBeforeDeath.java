package gedcom.validators;

import gedcom.Validator;
import gedcom.models.GEDFile;
import gedcom.models.Family;
import gedcom.models.Individual;

public class MarriageBeforeDeath extends Validator {

    public MarriageBeforeDeath(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        for (Family family : gedFile.getFamilies()) {

            Individual father = gedFile.getIndividual(family.getHusband().getID());
            Individual wife = gedFile.getIndividual(family.getWife().getID());

            if ((father.getDeath() != null && family.getMarriage().after(father.getDeath()))
                    || (wife.getDeath() != null && family.getMarriage().after(wife.getDeath()))) {
                System.out.printf("Error US05: Death of parent occurs before of marriage in family %s\n",
                        family.getID());
                valid = false;
            }
        }

        return valid;
    }

}
