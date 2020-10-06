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
            if ((father.getDeath() != null && family.getMarriage().after(father.getDeath())) || (wife.getDeath() != null && 
            	 family.getMarriage().after(wife.getDeath()))) {
                System.out.printf("Error US05: Death of parent occurs before of marriage in family %s\n", family.getID());
                valid = false;
            }
        }

        return valid;
    }

}
