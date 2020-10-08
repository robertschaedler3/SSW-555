package gedcom.validators;

import gedcom.models.GEDFile;

public class Sample extends Validator {

	public Sample(Validator validator) {
		super(validator);
	}

	protected boolean check(GEDFile gedFile) {
		boolean valid = true;
		// TODO
		return valid;
	}

}
