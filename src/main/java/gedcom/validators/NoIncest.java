package gedcom.validators;

import java.util.List;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class NoIncest extends Validator {

	public NoIncest(Validator validator) {
		super(validator);
	}

	private boolean isSiblings(Individual indi1, Individual indi2) {
		// Check if both people have the same FAMC, meaning that they have the same
		// parents and are siblings
		if (indi1.getChildFamilies().size() > 0 && indi2.getChildFamilies().size() > 0) {
			Family indi1Famc = indi1.getChildFamilies().get(0);
			Family indi2Famc = indi2.getChildFamilies().get(0);

			if (indi1Famc.equals(indi2Famc)) {
				return true;
			}
		}
		return false;
	}

	protected boolean check(GEDFile gedFile) {
		boolean valid = true;

		List<Family> families = gedFile.getFamilies();

		for (Family fam : families) {
			// if they're siblings, they're not first cousins
			if (isSiblings(fam.getHusband(), fam.getWife())) {
				System.out.printf("Anomaly US18: Siblings %s (%s) and %s (%s) are married, in family %s.\n",
						fam.getHusband().getName(), fam.getHusband().getID(), fam.getWife().getName(), fam.getWife().getID(), fam.getHusband().getChildrenFamily().get(0).getID());
				valid = false;
			} else if (fam.getHusband().getChildFamilies().size() > 0 && fam.getWife().getChildFamilies().size() > 0) {
				Individual indi1Dad = fam.getHusband().getChildFamilies().get(0).getHusband();
				Individual indi2Dad = fam.getWife().getChildFamilies().get(0).getHusband();
				Individual indi1Mom = fam.getHusband().getChildFamilies().get(0).getWife();
				Individual indi2Mom = fam.getWife().getChildFamilies().get(0).getWife();

				if (isSiblings(indi1Dad, indi2Mom) || isSiblings(indi1Dad, indi2Dad)
						|| isSiblings(indi1Mom, indi2Mom) || isSiblings(indi1Mom, indi2Dad)) {
					System.out.printf("Anomaly US19: First Cousins %s (%s) and %s (%s) are married, in family %s.\n",
							fam.getHusband().getName(), fam.getHusband().getID(), fam.getWife().getName(),
							fam.getWife().getID(), fam.getID());
					valid = false;
				}
			}
		}
		return valid;
	}

}
