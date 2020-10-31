package gedcom.validators;

import gedcom.models.GEDFile;
import gedcom.models.GEDObject;

import java.util.List;

import gedcom.logging.Error;
import gedcom.models.Family;
import gedcom.models.Individual;

public class CorrespondingEntries extends Validator {

    public CorrespondingEntries(Validator validator) {
        super(validator);
    }

    private void log(GEDObject... individual) {
        LOGGER.error(Error.CORRESPONDING_ENTRIES_NOT_FOUND, individual);
        valid = false;
    }

    protected boolean check(GEDFile gedFile) {

        List<Family> families = gedFile.getFamilies();
        List<Individual> individuals = gedFile.getIndividuals();

        for (Family family : families) {

            Individual husband = family.getHusband();
            Individual wife = family.getWife();

            if (husband != null && !individuals.contains(husband)) {
                log(family, husband);
            }

            if (wife != null && !individuals.contains(wife)) {
                log(family, wife);
            }

            for (Individual child : family.getChildren()) {
                if (child != null && !individuals.contains(child)) {
                    log(family, child);
                }
            }

        }

        for (Individual individual : individuals) {

            for (Family family : individual.getSpouseFamilies()) {
                if (family != null && !families.contains(family)) {
                    log(family, individual);
                }
            }

            for (Family family : individual.getChildFamilies()) {
                if (family != null && !families.contains(family)) {
                    log(family, individual);
                }
            }

        }

        return valid;
    }

}
