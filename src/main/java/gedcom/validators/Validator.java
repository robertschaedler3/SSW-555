package gedcom.validators;

import gedcom.logging.Logger;
import gedcom.models.GEDFile;

public abstract class Validator {

    protected Validator validator;

    protected boolean valid;

    protected Logger LOGGER = Logger.getInstance();

    public Validator(Validator validator) {
        this.validator = validator;
        this.valid = true;
    }

    protected abstract boolean check(GEDFile gedFile);

    public boolean isValid(GEDFile gedFile) {
        return this.check(gedFile) & this.validator.isValid(gedFile);
    }


}
