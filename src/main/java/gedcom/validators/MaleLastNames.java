package gedcom.validators;

import gedcom.interfaces.Gender;
import gedcom.logging.Error;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class MaleLastNames extends Validator {

    public MaleLastNames(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        for (Family family : gedFile.getFamilies()) {

            Individual husband;
            if ((husband = family.getHusband()) != null) {

                String lastName = husband.getLastName();
                for (Individual child : family.getChildren()) {
                    if (child.getGender() == Gender.M && !child.getLastName().equals(lastName)) {
                        LOGGER.anomaly(Error.MALE_LAST_NAMES_NOT_MATCHING, family, husband, child);
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }
}
