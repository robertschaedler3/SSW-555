package gedcom.validators;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class NoMarriagesToDescendants extends Validator {

    public NoMarriagesToDescendants(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        for (Family family : gedFile.getFamilies()) {
            Individual husband = family.getHusband();
            Individual wife = family.getWife();

            // Check husband
            if (wife.isDescendant(husband)) {
                System.out.printf("Error US17: Husband of Family %s (Individual %s) is married to a descendant.", husband.getID(), family.getID());
                valid = false;
            }

            // Check wife
            if (husband.isDescendant(wife)) {
                System.out.printf("Error US17: Wife of Family %s (Individual %s) is married to a descendant.", wife.getID(), family.getID());
                valid = false;
            }

        }

        return valid;
    }

}

