package gedcom.validators;

import gedcom.models.GEDFile;

public class ValidMarriage extends Validator {

    public ValidMarriage(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;
        // TODO
        return valid;
    }

}
