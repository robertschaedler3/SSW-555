package project.validators;

import project.Validator;
import project.models.GEDFile;

public class MarriageBeforeDeath extends Validator {

    public MarriageBeforeDeath(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;
        
        return valid;
    }

}
