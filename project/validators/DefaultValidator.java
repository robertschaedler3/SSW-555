package project.validators;

import project.Validator;
import project.models.GEDFile;

public class DefaultValidator extends Validator {

    public DefaultValidator() {
        super(null);
    }

    protected boolean check(GEDFile gedFile) {
        return true;
    }

    public boolean isValid(GEDFile gedFile) {
        return this.check(gedFile);
    }
}
