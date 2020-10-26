package gedcom.validators;

import gedcom.models.*;
import java.util.List;
/**
 * No more than one family with the same spouses by name 
 * and the same marriage date should appear in a GEDCOM file
 */
public class UniqueFamiliesBySpouses extends Validator {
    
    public UniqueFamiliesBySpouses(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        List<Family> families = gedFile.getFamilies();

        for (int i = 0; i < families.size(); i++) {
            Family current = families.get(i);
            for (int j = i+1; j < families.size(); j++) {
                Family check = families.get(j);
                if (current.getMarriage() == check.getMarriage() && current.getHusband() == check.getHusband() && current.getWife() == check.getWife()) {
                    System.out.println("Anomaly US24: No more than one family with the same spouses by name and the same marriage date should appear in GEDCOM file");
                    valid = false;
                }
            }
        }
        return valid;

    }
}
