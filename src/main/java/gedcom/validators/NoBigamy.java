package gedcom.validators;

import java.util.Date;
import java.util.List;

import gedcom.Validator;
import gedcom.models.*;

public class NoBigamy extends Validator {

	public NoBigamy(Validator validator) {
		super(validator);
	}

	protected boolean check(GEDFile gedFile) {
		boolean valid = true;

		List<Family> families = gedFile.getFamilies();

		for (int i = 0; i < families.size(); i++) {
			Family baseFam = families.get(i);
			// + 1 prevents from baseFam being checked against itself
			for (int j = i + 1; j < families.size(); j++) {
				Family testFam = families.get(j);
				// Only check marriages which share a husband or wife
				if (baseFam.getHusband().equals(testFam.getHusband()) || baseFam.getWife().equals(testFam.getWife())) {

					// Find earlier and later marriage
					boolean isBaseEarlier = baseFam.getMarriage().before(testFam.getMarriage());
					Family earlierFam = isBaseEarlier ? baseFam : testFam;
					Family laterFam = !isBaseEarlier ? baseFam : testFam;

					// If the earlier marriage has no divorce date, check death dates
					if (earlierFam.getDivorce() == null) {
						Date husbandDeath = earlierFam.getHusband().getDeath();
						Date wifeDeath = earlierFam.getHusband().getDeath();

						// if neither have died in the earlier marriage and there was no divorce, that's
						// bigamy
						if (husbandDeath == null && wifeDeath == null) {
							// if neither spouse died or died after the second marriage, that's bigamy
							System.out.println("Anomaly US11: Bigamy: Marriage " + earlierFam.getID()
									+ " never got divorced and neither spouse died.");
							valid = false;
						}
						// if either spouse DID die, make sure it's before later marriage
						else if ((husbandDeath != null && husbandDeath.before(laterFam.getMarriage()))
								|| (wifeDeath != null && wifeDeath.before(laterFam.getMarriage()))) {
							System.out.println("Anomaly US11: One spouse died in marriage " + earlierFam.getID()
									+ " - not bigamy");
							valid = false;
						}
					}
					// if the earlier marriage DOES have a divorce date, it must be before the later
					// marriage date
					else if (earlierFam.getDivorce().after(laterFam.getMarriage())) {
						System.out.println("Anomaly US11: Bigamy: Earlier marriage " + earlierFam.getID()
								+ " divorced on " + earlierFam.getDivorce() + " which is after " + laterFam.getID()
								+ " marriage date " + laterFam.getMarriage());
						valid = false;
					}
				}
			}
		}

		return valid;
	}

}
