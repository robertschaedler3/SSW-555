package gedcom.validators;

import gedcom.logging.Error;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class NoIncest extends Validator {

	public NoIncest(Validator validator) {
		super(validator);
	}

	private void checkMarriageToCousin(Individual i1, Individual i2) {
		for (Individual sibling : i1.getSiblings()) {
			if (sibling.getChildren().contains(i2)) {
				LOGGER.anomaly(Error.MARRIAGE_TO_FIRST_COUSIN, i2, i1);
				valid = false;
			}
		}
	}

	protected boolean check(GEDFile gedFile) {
		for (Family family : gedFile.getFamilies()) {
            Individual husband = family.getHusband();
			Individual wife = family.getWife();
			if (husband != null && wife != null) {
				if (husband.isSibling(wife)) {
					LOGGER.anomaly(Error.MARRIAGE_TO_SIBLING, family, husband, wife);
					valid = false;
				} else if (husband.getCousins(1).contains(wife)) {
					LOGGER.anomaly(Error.MARRIAGE_TO_FIRST_COUSIN, family, husband, wife);
					valid = false;
				}
				
				checkMarriageToCousin(husband, wife);
				checkMarriageToCousin(wife, husband);
			}
		}
		
		return valid;
	}

}
