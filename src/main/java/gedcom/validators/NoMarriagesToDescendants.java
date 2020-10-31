package gedcom.validators;

import gedcom.logging.Error;
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
                LOGGER.anomaly(Error.MARRIAGE_TO_SIBLING, family, husband, wife);
                valid = false;
            }
            
            // Check wife
            if (husband.isDescendant(wife)) {
                LOGGER.anomaly(Error.MARRIAGE_TO_SIBLING, family, husband, wife);
                valid = false;
            }
        }

        return valid;
    }

}

