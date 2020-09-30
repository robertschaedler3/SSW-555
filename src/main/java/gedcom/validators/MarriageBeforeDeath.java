package gedcom.validators;

import java.util.Map;

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
        Map<String, Individual> individuals = gedFile.getIndividuals();

        for (Map.Entry<String, Family> entry : gedFile.getFamilies().entrySet()) {
            Family family = entry.getValue();
            Individual father = individuals.get(family.getHusband());
            Individual wife = individuals.get(family.getWife());
            if ((family.getMarriage().after(father.getDeath())) || (family.getMarriage().after(wife.getDeath()))) {
                System.out.printf("Death of parent occurs before of marriage in family %s", family.getID());
                valid = false;
            }
        }

        return valid;
    }

}
