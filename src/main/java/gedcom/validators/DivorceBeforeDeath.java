package gedcom.validators;

import gedcom.logging.Error;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import java.util.Date;

public class DivorceBeforeDeath extends Validator {

    public DivorceBeforeDeath(Validator validator) {
        super(validator);
    }

    private void check(Date divorce, Individual individual, Family family) {
        if (individual != null && individual.getDeath() != null && divorce.after(individual.getDeath())) {
            LOGGER.error(Error.DEATH_BEFORE_DIVORCE, family, individual);
            valid = false;
        }
    }

    protected boolean check(GEDFile gedFile) {

        for (Family family : gedFile.getFamilies()) {
            
            Individual husband = family.getHusband();
            Individual wife = family.getWife();
            Date divorce = family.getDivorce();

            if (divorce != null) {
                check(divorce, husband, family);
                check(divorce, wife, family);
            }
        }

        return valid;
    }

}
