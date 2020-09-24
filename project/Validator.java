package project;

import project.models.GEDCOM;

public abstract class Validator {

    protected Validator validator;

    public Validator(Validator validator) {
        this.validator = validator;
    }

    protected abstract boolean check(GEDCOM gedcom);

    public boolean isValid(GEDCOM gedcom) {
        return this.check(gedcom) & this.validator.isValid(gedcom);
    }

}
