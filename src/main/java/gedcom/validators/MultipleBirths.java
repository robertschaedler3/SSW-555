package gedcom.validators;

import java.util.Map;
import java.util.HashMap;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class MultipleBirths extends Validator {

    public MultipleBirths(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        Map<String, Integer> siblingsWithBirthday = new HashMap<>();

        for (Family family : gedFile.getFamilies()) {
            for (Individual child : family.getChildren()) {
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
            if (siblings > Family.MAX_MULTIPLE_BIRTH) {
                System.out.printf("Anomaly US14: More than %d siblings born on %s", Family.MAX_MULTIPLE_BIRTH, entry.getKey());
                valid = false;
            }
        }

        return valid;
    }

}
