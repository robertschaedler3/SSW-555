package gedcom.validators;

import java.util.List;
import java.util.Map;

import gedcom.Validator;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class SiblingSpacing extends Validator {

    public SiblingSpacing(Validator validator) {
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
                for (int j = i + 1; (child.getBirthday() != null) && (j < children.size()); j++) {
                    Individual sibling = individuals.get(children.get(j));
                    if (sibling.getBirthday() != null) {
                        long diff = Math.abs(child.getBirthday().getTime() - sibling.getBirthday().getTime());
                        long diffInDays = (diff / (1000 * 60 * 60 * 24)) % 365;
                        if (diffInDays > 2 && diffInDays < (30 * 8)) {
                            System.out.printf("Individual %s was born more the 2 days and less than 8 months from individual %s.\n", child.getID(), sibling.getID());
                            valid = false;
                        }
                    }
                }
            }
        }

        return valid;
    }

}
