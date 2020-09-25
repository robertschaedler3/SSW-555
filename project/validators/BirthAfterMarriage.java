package project.validators;

import java.util.Map;

import project.Validator;
import project.models.Family;
import project.models.GEDFile;
import project.models.Individual;

public class BirthAfterMarriage extends Validator {

    public BirthAfterMarriage(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;
        Map<String, Individual> individuals = gedFile.getIndividuals();

        for (Map.Entry<String, Family> entry : gedFile.getFamilies().entrySet()) {
            Family family = entry.getValue();
            for (String childID : family.getChildren()) {
                Individual child = individuals.get(childID);
                if (child.getBirthday().after(family.getMarriage())) {
                    System.out.printf("Birth of child %s occurs before of marriage in family %s", child.getID(), family.getID());
                    valid = false;
                }
            }
        }

        return valid;
    }
}
