package gedcom.validators;

import java.util.Map;

import gedcom.Validator;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

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
                System.out.printf("Anomaly US07: %s's (%s) age is >= 150\n", individual.getName(), individual.getID());
                valid = false;
            }
        }

        return valid;
    }

}