package project.validators;

import project.Validator;
import project.models.GEDCOM;

public class NoBigamy extends Validator {

	public NoBigamy(Validator validator) {
		super(validator);
	}

	protected boolean check(GEDCOM gedcom) {
		boolean valid = true;
		// TODO
		return valid;
	}

}
