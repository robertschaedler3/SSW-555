package gedcom.validators;

import java.util.Map;
import java.util.HashMap;

import gedcom.Validator;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class MultipleBirths extends Validator {

    public MultipleBirths(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        Map<String, Individual> individuals = gedFile.getIndividuals();
        Map<String, Integer> siblingsWithBirthday = new HashMap<>();

        for (Map.Entry<String, Family> entry : gedFile.getFamilies().entrySet()) {
            Family family = entry.getValue();
            for (String childID : family.getChildren()) {
                Individual child = individuals.get(childID);
                String birthday = child.getBirthday().toString();
                if (siblingsWithBirthday.containsKey(birthday)) {
                    siblingsWithBirthday.replace(birthday, siblingsWithBirthday.get(birthday) + 1);
                } else {
                    siblingsWithBirthday.put(birthday, 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : siblingsWithBirthday.entrySet()) {
            Integer siblings = entry.getValue();
            if (siblings > 5) {
                System.out.printf("Anomaly US14: More than 5 siblings born on %s", entry.getKey());
                valid = false;
            }
        }

        return valid;
    }

}
