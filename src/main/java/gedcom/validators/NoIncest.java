package gedcom.validators;

import gedcom.logging.Error;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.GEDObject;
import gedcom.models.Individual;

public class NoIncest extends Validator {

	public NoIncest(Validator validator) {
		super(validator);
	}

	private void log(Error error, GEDObject... objs) {
		LOGGER.anomaly(error, objs);
		valid = false;
	}

	private void checkMarriageToSiblings(Family family, Individual i1, Individual i2) {
		if (i1.isSibling(i2)) {
			log(Error.MARRIAGE_TO_SIBLING, family, i1, i2);
		}
	}

	private void checkMarriageToCousin(Family family, Individual i1, Individual i2) {
		if (i1.getCousins().contains(i2)) {
			log(Error.MARRIAGE_TO_FIRST_COUSIN, family, i1, i2);
		}
	}

	private void checkMarriageToNieceNephew(Family family, Individual i1, Individual i2) {
		for (Individual sibling : i1.getSiblings()) {
			if (sibling.getChildren().contains(i2)) {
				log(Error.MARRIAGE_TO_NIECE_NEPHEW, family, i1, i2);
			}
		}
	}

	private void checkMarriageToDescendants(Family family, Individual i1, Individual i2) {
		if (i1.isDescendant(i2)) {
			log(Error.MARRIAGE_TO_DESCENDANT, family, i2, i1);
		}
	}


	protected boolean check(GEDFile gedFile) {
		for (Family family : gedFile.getFamilies()) {

            Individual husband = family.getHusband();
			Individual wife = family.getWife();

			if (husband != null && wife != null) {

				checkMarriageToSiblings(family, husband, wife);

				checkMarriageToCousin(family, husband, wife);

				checkMarriageToNieceNephew(family, husband, wife);
				checkMarriageToNieceNephew(family, wife, husband);

				checkMarriageToDescendants(family, husband, wife);
				checkMarriageToDescendants(family, wife, husband);
			}
		}
		
		return valid;
	}

}
