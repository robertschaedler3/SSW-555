package gedcom.validators;

import java.util.Date;
import java.util.List;

import gedcom.logging.Error;
import gedcom.models.*;

public class NoBigamy extends Validator {

	public NoBigamy(Validator validator) {
		super(validator);
	}

	private boolean familiesShareSpouse(Family f1, Family f2) {
		Individual h1 = f1.getHusband();
		Individual h2 = f2.getHusband();
		Individual w1 = f1.getHusband();
		Individual w2 = f2.getHusband();

		return (h1 != null && h2 != null && h1.equals(h2)) ? true
				: ((w1 != null && w2 != null && w1.equals(w2)) ? true : false);
	}

	private void checkDeathBeforeMarriage(Family firstFamily, Family secondFamily, Individual individual) {
		if (individual == null) {
			return;
		}
		Date death = individual.getDeath();
		Date marriage = secondFamily.getMarriage();
		Date divorce = firstFamily.getDivorce();
		if (death != null && marriage != null && divorce == null) {
			if (death.after(marriage)) {
				log(firstFamily, secondFamily, individual);
			}
		}
	}

	private void checkDivorceBeforeMarriage(Family firstFamily, Family secondFamily, Individual individual) {
		if (individual == null) {
			return;
		}
		Date marriage = secondFamily.getMarriage();
		Date divorce = firstFamily.getDivorce();
		if (marriage != null && divorce != null) {
			if (divorce.after(marriage)) {
				log(firstFamily, secondFamily, individual);
			}
		}
	}

	private void log(GEDObject... objs) {
		LOGGER.anomaly(Error.BIGAMY, objs);
		valid = false;
	}

	protected boolean check(GEDFile gedFile) {
		List<Family> families = gedFile.getFamilies();

		for (int i = 0; i < families.size(); i++) {

			Family baseFamily = families.get(i);

			for (int j = i + 1; j < families.size(); j++) {

				Family testFamily = families.get(j);

				// Check families that share a spouse
				if (familiesShareSpouse(baseFamily, testFamily)) {

					// Get earlier and later marriage
					boolean isBaseEarlier = baseFamily.getMarriage().before(testFamily.getMarriage());
					Family firstFamily = isBaseEarlier ? baseFamily : testFamily;
					Family secondFamily = !isBaseEarlier ? baseFamily : testFamily;

					Individual husband = firstFamily.getHusband();
					Individual wife = secondFamily.getWife();

					if (firstFamily.getDivorce() == null) { // check death dates
						
						Date husbandDeath = firstFamily.getHusband().getDeath();
						Date wifeDeath = secondFamily.getHusband().getDeath();

						if (husbandDeath == null && wifeDeath == null) { // neither spouse died before second marriage
							log(firstFamily, secondFamily);
						} else {
							checkDeathBeforeMarriage(firstFamily, secondFamily, husband);
							checkDeathBeforeMarriage(firstFamily, secondFamily, wife);
						}
					} else {
						checkDivorceBeforeMarriage(firstFamily, secondFamily, husband);
						checkDivorceBeforeMarriage(firstFamily, secondFamily, wife);
					}
				}
			}
		}

		return valid;
	}

}
