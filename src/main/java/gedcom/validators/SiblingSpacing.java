package gedcom.validators;

import gedcom.Validator;
import gedcom.models.GEDFile;

public class SiblingSpacing extends Validator {

    public SiblingSpacing(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedfile) {
        return true;
    }

}
