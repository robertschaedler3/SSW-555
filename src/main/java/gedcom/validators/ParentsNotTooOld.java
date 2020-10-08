package gedcom.validators;

import java.util.Date;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class ParentsNotTooOld extends Validator {

	private final int FATHER_THRESHOLD = 80;
	private final int MOTHER_THRESHOLD = 60;

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
		boolean valid = true;

		for (Family family : gedFile.getFamilies()) {

			Individual father = family.getHusband();
			Individual mother = family.getWife();

			for (Individual child : family.getChildren()) {
				if (parentAgeWhenChildBorn(father, child) > FATHER_THRESHOLD) {
					System.out.println("Anomaly US12: Father too old: Parent " + father.getName() + "(" + father.getID()
							+ ") is more than " + FATHER_THRESHOLD + " years older than child " + child.getName() + "("
							+ child.getID() + ")");
					valid = false;
				}
				if (parentAgeWhenChildBorn(mother, child) > MOTHER_THRESHOLD) {
					System.out.println("Anomaly US12: Mother too old: Parent " + mother.getName() + "(" + mother.getID()
							+ ") is more than " + MOTHER_THRESHOLD + " years older than child " + child.getName() + "("
							+ child.getID() + ")");
					valid = false;
				}
			}
		}

		return valid;
	}

}
