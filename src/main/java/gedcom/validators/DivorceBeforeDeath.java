package gedcom.validators;

import gedcom.Validator;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import java.util.Date;
import java.util.Map;

public class DivorceBeforeDeath extends Validator {

    public DivorceBeforeDeath(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        Map<String, Individual> individuals = gedFile.getIndividuals();

        for (Map.Entry<String, Family> entry : gedFile.getFamilies().entrySet()) {
            Family family = entry.getValue();
            Date divorce = family.getDivorce();
            Date husbandDeath = individuals.get(family.getHusband()).getDeath();
            Date wifeDeath = individuals.get(family.getWife()).getDeath();

            if ((divorce != null) && divorce.after(husbandDeath)) {
                System.out.printf("Divorce of Family %s occurs after the death of Individual %s\n", family.getID(), family.getHusband());
                valid = false;
            }

            if ((divorce != null) && divorce.after(wifeDeath)) {
                System.out.printf("Divorce of Family %s occurs after the death of Individual %s\n", family.getID(), family.getWife());
                valid = false;
            }
        }

        return valid;
    }

}
