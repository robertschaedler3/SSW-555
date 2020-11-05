package project.validators;

import project.Validator;
import project.models.GEDFile;

public class NoBigamy extends Validator {

	public NoBigamy(Validator validator) {
		super(validator);
	}

	protected boolean check(GEDFile gedFile) {
		boolean valid = true;
		// TODO
		return valid;
	}

}
