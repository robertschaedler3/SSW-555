package gedcom.validators;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import gedcom.Validator;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class ValidBirth extends Validator {

    public ValidBirth(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;
        Map<String, Individual> individuals = gedFile.getIndividuals();

        for (Map.Entry<String, Family> entry : gedFile.getFamilies().entrySet()) {
            Family family = entry.getValue();
            List<String> children = family.getChildren(); 
            for (int i = 0; i < children.size(); i++) {
                Individual child = individuals.get(children.get(i));
                Individual mother = individuals.get(family.getWife());
                Individual father = individuals.get(family.getHusband());
                if (child.getBirthday() != null) {
                    if (family.getMarriage() != null && child.getBirthday().before(family.getMarriage())) {
                        System.out.printf("Anomaly US08: Birth of child %s (%s) occurs before of marriage in family %s\n", child.getName(), child.getID(), family.getID());
                        valid = false;
                    }
                    if (mother.getDeath() != null && child.getBirthday().after(mother.getDeath())) {
                        System.out.printf("Error US09: Birth of child %s (%s) occurs after death of mother %s\n", child.getName(), child.getID(), mother.getID());
                        valid = false;
                    }
                    if (father.getDeath() != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(father.getDeath());
                        calendar.add(Calendar.MONTH, 9);
                        if (child.getBirthday().after(calendar.getTime())) {
                            System.out.printf("Error US09: Birth of child %s (%s) occurs 9+ months after death of father %s\n", child.getName(), child.getID(), father.getID());
                            valid = false;
                        }
                    }
                    for (int j = i + 1; j < children.size(); j++) {
                        Individual sibling = individuals.get(children.get(j));
                        if (sibling.getBirthday() != null) {
                            long diff = Math.abs(child.getBirthday().getTime() - sibling.getBirthday().getTime());
                            long diffInDays = (diff / (1000 * 60 * 60 * 24)) % 365;
                            if (diffInDays > 2 && diffInDays < (30 * 8)) {
                                System.out.printf("Anomaly US13: Individual %s (%s) was born more than 2 days and less than 8 months from individual %s (%s).\n", child.getName(), child.getID(), sibling.getName(), sibling.getID());
                                valid = false;
                            }
                        }
                    }
                }
            }
        }

        return valid;
    }
}
