package project.validators;

import java.util.Map;

import project.Validator;
import project.models.GEDFile;
import project.models.Family;

public class MarriageBeforeDivorce extends Validator {

	public MarriageBeforeDivorce(Validator validator) {
		super(validator);
	}

	protected boolean check(GEDFile gedFile) {
        boolean valid = true;
        
        for (Map.Entry<String, Family> entry : gedFile.getFamilies().entrySet()) {
            Family family = entry.getValue();
            if(family.getMarriage().after(family.getDivorce())){
                System.out.printf("Divorce of family occurs before of marriage in family %s", family.getID());
                valid = false;
            }
        }
		return valid;
	}

}

