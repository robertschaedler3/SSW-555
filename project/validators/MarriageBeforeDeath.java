package project.validators;

import java.util.Map;

import project.Validator;
import project.models.GEDFile;
import project.models.Family;
import project.models.Individual;

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
            if((father.getDeath().after(family.getMarriage())) || (wife.getDeath().after(family.getMarriage()))){
                System.out.printf("Death of parent occurs before of marriage in family %s", family.getID());
                valid = false;
            }
        }

        return valid;
    }

}
