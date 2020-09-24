package project.validators;

import project.Validator;
import project.models.GEDCOM;

public class DefaultValidator extends Validator {

    public DefaultValidator() {
        super(null);
    }

    protected boolean check(GEDCOM gedcom) {
        return true;
    }

    public boolean isValid(GEDCOM gedcom) {
        return this.check(gedcom);
    }
}
