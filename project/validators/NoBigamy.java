package project.validators;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import project.Validator;
import project.models.*;

public class NoBigamy extends Validator {

	public NoBigamy(Validator validator) {
		super(validator);
	}

	protected boolean check(GEDFile gedFile) {
		boolean valid = true;

		Map<String, Family> families = gedFile.getFamilies();
		Map<String, Individual> individuals = gedFile.getIndividuals();
		
		Collection<Family> familyCollection = families.values();
		Family[] familyArr = familyCollection.toArray(new Family[familyCollection.size()]);

		for (int i = 0; i < families.values().size(); i++) {
		    Family baseFam = familyArr[i];
		    // + 1 prevents from baseFam being checked against itself
		    for (int j = i + 1; j < families.values().size(); j++) {
		    	Family testFam = familyArr[j];
					// Only check marriages which share a husband or wife
					if (baseFam.getHusband().equals(testFam.getHusband())
							|| baseFam.getWife().equals(testFam.getWife())) {

						// Find earlier and later marriage
						boolean isBaseEarlier = baseFam.getMarriage().before(testFam.getMarriage());
						Family earlierFam = isBaseEarlier ? baseFam : testFam;
						Family laterFam = !isBaseEarlier ? baseFam : testFam;

						// If the earlier marriage has no divorce date, check death dates
						if (earlierFam.getDivorce() == null) {
							Date husbandDeath = individuals.get(earlierFam.getHusband()).getDeath();
							Date wifeDeath = individuals.get(earlierFam.getHusband()).getDeath();

							// if neither have died in the earlier marriage and there was no divorce, that's
							// bigamy
							if (husbandDeath == null && wifeDeath == null) {
								// if neither spouse died or died after the second marriage, that's bigamy
								System.out.println("Bigamy: Marriage " + earlierFam.getID()
										+ " never got divorced and neither spouse died.");
								valid = false;
							}
							// if either spouse DID die, make sure it's before later marriage
							else if ((husbandDeath != null && husbandDeath.before(laterFam.getMarriage()))
									|| (wifeDeath != null && wifeDeath.before(laterFam.getMarriage()))) {
								System.out
										.println("One spouse died in marriage " + earlierFam.getID() + " - not bigamy");
							}
						} 
						// if the earlier marriage DOES have a divorce date, it must be before the later
						// marriage date
						else if (earlierFam.getDivorce().after(laterFam.getMarriage())) {
							System.out.println("Bigamy: Earlier marriage " + earlierFam.getID() + " divorced on "
									+ earlierFam.getDivorce() + " which is after " + laterFam.getID()
									+ " marriage date " + laterFam.getMarriage());
							valid = false;
						} 
						else {
							System.out.println(earlierFam.getDivorce() + " is before " + laterFam.getMarriage() + " - not bigamy");
						}
					}
				}
			}

		return valid;
	}

}
