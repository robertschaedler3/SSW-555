package gedcom.validators;

import java.util.Collection;
import java.util.Map;

import gedcom.Validator;
import gedcom.interfaces.Gender;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class ParentsNotTooOld extends Validator {

	public ParentsNotTooOld(Validator validator) {
		super(validator);
	}

	protected boolean check(GEDFile gedFile) {
		boolean valid = true;

		Map<String, Individual> individuals = gedFile.getIndividuals();

		for (Individual parent : individuals.values()) {
			Collection<Individual> children = parent.getChildren(individuals.values());
			if (children != null && children.size() > 0) {
				int threshold = parent.getGender() == Gender.M ? 80 : 60;

				for (Individual child : children) {
					if (parent.age() - child.age() > threshold) {
						System.out.println("Anomaly US12: Parent too old: Parent " + parent.getName() + "(" + parent.getID() + ") is more than " + threshold
								+ " years older than child " + child.getName() + "(" + child.getID() + ")");
						valid = false;
					}
				}
			}
		}

		return valid;
	}

}
