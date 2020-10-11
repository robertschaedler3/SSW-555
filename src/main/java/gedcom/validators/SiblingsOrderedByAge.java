package gedcom.validators;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class SiblingsOrderedByAge extends Validator {
 
    public SiblingsOrderedByAge(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;
        
        for (Family family : gedFile.getFamilies()) {
            Individual lastChild = null;
            for (Individual currentChild : family.getChildren()) {
                if (lastChild == null || lastChild.getBirthday().before(currentChild.getBirthday()) || lastChild.getBirthday().equals(currentChild.getBirthday())) {
                    lastChild = currentChild;
                } else {
                    System.out.printf("Error US28: Sibling %s (individual %s) occurs before %s (individual %s) but has a later birthday\n", lastChild.getName(), lastChild.getID(), currentChild.getName(), currentChild.getID());
                    valid = false;
                }
            }
        }
        
        return valid;
    }
    
}
