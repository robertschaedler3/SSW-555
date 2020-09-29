package project.validators;

import java.util.Calendar;
import java.util.Map;

import project.Validator;
import project.models.Family;
import project.models.GEDFile;
import project.models.Individual;

public class ValidBirth extends Validator {

    public ValidBirth(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;
        Map<String, Individual> individuals = gedFile.getIndividuals();

        for (Map.Entry<String, Family> entry : gedFile.getFamilies().entrySet()) {
            Family family = entry.getValue();
            for (String childID : family.getChildren()) {
                Individual child = individuals.get(childID);
                Individual mother = individuals.get(family.getWife());
                Individual father = individuals.get(family.getHusband());
                if (child.getBirthday() != null) {
                    if (family.getMarriage() != null && child.getBirthday().before(family.getMarriage())) {
                        System.out.printf("Birth of child %s occurs before of marriage in family %s\n", child.getID(), family.getID());
                        valid = false;
                    }
                    if (mother.getDeath() != null && child.getBirthday().after(mother.getDeath())) {
                        System.out.printf("Birth of child %s occurs after death of mother %s\n", child.getID(), mother.getID());
                        valid = false;
                    }
                    if (father.getDeath() != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(father.getDeath());
                        calendar.add(Calendar.MONTH, 9);
                        if (child.getBirthday().after(calendar.getTime())) {
                            System.out.printf("Birth of child %s occurs 9+ months after death of father %s\n", child.getID(), father.getID());
                            valid = false;
                        }
                    }
                }
            }
        }

        return valid;
    }
}
