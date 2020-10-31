package gedcom.validators;

import java.util.Map;
import java.util.HashMap;

import gedcom.logging.Error;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class MultipleBirths extends Validator {

    public MultipleBirths(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        Map<String, Integer> siblingsWithBirthday = new HashMap<>();
        for (Family family : gedFile.getFamilies()) {

            siblingsWithBirthday.clear();

            for (Individual child : family.getChildren()) {
                String birthday = child.getBirthday().toString();
                if (siblingsWithBirthday.containsKey(birthday)) {
                    siblingsWithBirthday.replace(birthday, siblingsWithBirthday.get(birthday) + 1);
                } else {
                    siblingsWithBirthday.put(birthday, 1);
                }
            }

            for (Map.Entry<String, Integer> entry : siblingsWithBirthday.entrySet()) {
                Integer siblings = entry.getValue();
                if (siblings > Family.MAX_MULTIPLE_BIRTH) {
                    LOGGER.anomaly(Error.MAX_MULTIPLE_SIMULTANEOUS_BIRTHS_EXCEEDED, family);
                    valid = false;
                }
            }
        }
        
        return valid;
    }

}
