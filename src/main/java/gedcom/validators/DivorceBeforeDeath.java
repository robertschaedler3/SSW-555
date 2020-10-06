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

            if ((divorce != null && husbandDeath != null) && divorce.after(husbandDeath)) {
                System.out.printf("Error US06: Divorce of Family %s occurs after the death of %s (Individual %s)\n", family.getID(), gedFile.getIndividualById(family.getHusband()).getName(), family.getHusband());
                valid = false;
            }

            if ((divorce != null && wifeDeath != null) && divorce.after(wifeDeath)) {
                System.out.printf("Error US06: Divorce of Family %s occurs after the death of %s (Individual %s)\n", family.getID(), gedFile.getIndividualById(family.getHusband()).getName(), family.getWife());
                valid = false;
            }
        }

        return valid;
    }

}
