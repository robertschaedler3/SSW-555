package gedcom.validators;

import java.util.List;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class NoIncest extends Validator {

	public NoIncest(Validator validator) {
		super(validator);
	}

	private boolean isSiblings(Individual indi1, Individual indi2, Boolean reportSiblings) {
		// Check if both people have the same FAMC, meaning that they have the same
		// parents and are siblings
		if (indi1.getChildrenFamily().size() > 0 && indi2.getChildrenFamily().size() > 0) {
			Family indi1Famc = indi1.getChildrenFamily().get(0);
			Family indi2Famc = indi2.getChildrenFamily().get(0);

			if (indi1Famc.equals(indi2Famc)) {
				if (reportSiblings) {
					System.out.printf("Anomaly US18: Siblings %s (%s) and %s (%s) are married, in family %s.\n",
							indi1.getName(), indi1.getID(), indi2.getName(), indi2.getID(), indi1Famc.getID());
				}
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
			if (isSiblings(fam.getHusband(), fam.getWife(), true)) {
				valid = false;
			} else if (fam.getHusband().getChildrenFamily().size() > 0 && fam.getWife().getChildrenFamily().size() > 0) {
				Individual indi1Dad = fam.getHusband().getChildrenFamily().get(0).getHusband();
				Individual indi2Dad = fam.getWife().getChildrenFamily().get(0).getHusband();
				Individual indi1Mom = fam.getHusband().getChildrenFamily().get(0).getWife();
				Individual indi2Mom = fam.getWife().getChildrenFamily().get(0).getWife();

				if (isSiblings(indi1Dad, indi2Mom, false) || isSiblings(indi1Dad, indi2Dad, false)
						|| isSiblings(indi1Mom, indi2Mom, false) || isSiblings(indi1Mom, indi2Dad, false)) {
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
