package gedcom.validators;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class NoIncest extends Validator {

	public NoIncest(Validator validator) {
		super(validator);
	}

	protected boolean check(GEDFile gedFile) {
		boolean valid = true;

		for (Family family : gedFile.getFamilies()) {
            Individual husband = family.getHusband();
            Individual wife = family.getWife();
            if (husband.isSibling(wife)) {
                System.out.printf("Anomaly US18: Siblings %s and %s are married, in family %s.\n", husband, wife, family);
                valid = false;
            } else if (husband.getCousins(1).contains(wife)) {
                System.out.printf("Anomaly US19: First cousins %s and %s are married, in family %s.\n", husband, wife, family);
                valid = false;
            } 
			
			for (Individual sibling : husband.getSiblings()) {
				if (sibling.getChildren().contains(wife)) {
					System.out.printf("Anomaly US20: Uncle %s is married to their niece %s.", husband, wife);
					valid = false;
				}
			}
			
			for (Individual sibling : wife.getSiblings()) {
				if (sibling.getChildren().contains(husband)) {
					System.out.printf("Anomaly US20: Aunt %s is married to their nephew %s.", wife, husband);
					valid = false;
				}
			}
			
		}
		
		return valid;
	}

}
