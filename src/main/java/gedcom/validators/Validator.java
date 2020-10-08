package gedcom.validators;

import gedcom.models.GEDFile;

public abstract class Validator {

    protected Validator validator;

    public Validator(Validator validator) {
        this.validator = validator;
    }

    protected abstract boolean check(GEDFile gedFile);

    public boolean isValid(GEDFile gedFile) {
        return this.check(gedFile) & this.validator.isValid(gedFile);
    }

}
