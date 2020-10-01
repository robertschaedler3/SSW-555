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
            List<String> siblings = family.getChildren();
            for (int i = 0; i < siblings.size(); i++) {
                Individual individual = individuals.get(siblings.get(i));
                for (int j = i + 1; (individual.getBirthday() != null) && (j < siblings.size()); j++) {
                    Individual sibling = individuals.get(siblings.get(j));
                    if (sibling.getBirthday() != null) {
                        long diff = Math.abs(individual.getBirthday().getTime() - sibling.getBirthday().getTime());
                        long diffInDays = (diff / (1000 * 60 * 60 * 24)) % 365;
                        if (diffInDays > 2 && diffInDays < (30 * 8)) {
                            System.out.printf("Individual %s was born more the 2 days and less than 8 months from individual %s.\n", individual.getID(), sibling.getID());
                            valid = false;
                        }
                    }
                }
            }
        }

        return valid;
    }

}
