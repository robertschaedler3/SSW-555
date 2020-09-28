package project.validators;

import java.util.Map;

import project.Validator;
import project.models.GEDFile;
import project.models.Individual;

public class LessThan150YearsOld extends Validator {

    public LessThan150YearsOld(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        Map<String, Individual> individuals = gedFile.getIndividuals();
        for (Map.Entry<String, Individual> entry : individuals.entrySet()) {
            Individual individual = entry.getValue();
            if (individual.age() >= 150) {
                System.out.printf("%s's age is >= 150", individual.getName());
                valid = false;
            }
        }

        return valid;
    }

}