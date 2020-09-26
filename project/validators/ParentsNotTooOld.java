package project.validators;

import java.util.List;
import java.util.Map;

import project.Validator;
import project.interfaces.Gender;
import project.models.Family;
import project.models.GEDFile;
import project.models.Individual;

public class ParentsNotTooOld extends Validator {

	public ParentsNotTooOld(Validator validator) {
		super(validator);
	}

	protected boolean check(GEDFile gedFile) {
		boolean valid = true;
		
		Map<String, Individual> individuals = gedFile.getIndividuals();
		
		for (Individual parent : individuals.values()) {
			List<Individual> children = parent.getChildren(individuals.values().toArray(new Individual[individuals.values().size()]));
			if(children != null && children.size() > 0) {
				int threshold = parent.getGender() == Gender.M ? 80 : 60;
				
				for (Individual child : children) {
					if(parent.age() - child.age() > threshold) {
						System.out.println("Parent too old: Parent " + parent.getID() + " is more than " + threshold + " years older than child " + child.getID());
						valid = false;
					}
				}
			}
		}
		
		return valid;
	}

}
