package gedcom.validators;

import gedcom.interfaces.Gender;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class MaleLastNames extends Validator {

    public MaleLastNames(Validator validator) {
        super(validator);
    }


    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        for (Family family : gedFile.getFamilies()) {
            String lastName = family.getHusband().getLastName();

            for (Individual child : family.getChildren()) {
                if (child.getGender() == Gender.M) {
                    if (!child.getLastName().equals(lastName)) {
                        valid = false;
                        System.out.printf("Anomaly US16: Individual %s does not share father's last name of '%s'", child.getID(), lastName);
                    }
                }
            }

        }

        return valid;
    }
}
