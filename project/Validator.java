package project;

import project.models.GEDFile;

public abstract class Validator {

    protected Validator validator;

    public Validator(Validator validator) {
        this.validator = validator;
    }

    protected abstract boolean check(GEDFile gedcom);

    public boolean isValid(GEDFile gedcom) {
        return this.check(gedcom) & this.validator.isValid(gedcom);
    }

}
