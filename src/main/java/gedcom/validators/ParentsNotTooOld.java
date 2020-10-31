package gedcom.validators;

import java.util.Date;

import gedcom.logging.Error;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class ParentsNotTooOld extends Validator {

	public ParentsNotTooOld(Validator validator) {
		super(validator);
	}

	private int parentAgeWhenChildBorn(Individual parent, Individual child) {
		long diffInYears = 0;
		if (parent == null || child == null) {
			return (int) diffInYears;
		}

		Date parentBirthday = parent.getBirthday();
		Date childBirthday = child.getBirthday();
		if (parentBirthday != null && childBirthday != null && parentBirthday.before(childBirthday)) {
			diffInYears = (childBirthday.getTime() - parentBirthday.getTime()) / (1000l * 60 * 60 * 24 * 365);
		}
		return (int) diffInYears;
	}

	protected boolean check(GEDFile gedFile) {

		for (Family family : gedFile.getFamilies()) {

			Individual father = family.getHusband();
			Individual mother = family.getWife();

			for (Individual child : family.getChildren()) {
				if (parentAgeWhenChildBorn(father, child) > Family.FATHER_AGE_THRESHOLD) {
					LOGGER.anomaly(Error.PARENT_BIRTH_THRESHOLD_EXCEEDED, family, father, child);
					valid = false;
				}
				if (parentAgeWhenChildBorn(mother, child) > Family.MOTHER_AGE_THRESHOLD) {
					LOGGER.anomaly(Error.PARENT_BIRTH_THRESHOLD_EXCEEDED, family, mother, child);
					valid = false;
				}
			}
		}

		return valid;
	}

}
