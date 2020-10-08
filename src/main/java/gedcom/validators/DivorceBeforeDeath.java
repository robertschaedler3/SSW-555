package gedcom.validators;

import gedcom.models.Family;
import gedcom.models.GEDFile;

import java.util.Date;

public class DivorceBeforeDeath extends Validator {

    public DivorceBeforeDeath(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        for (Family family : gedFile.getFamilies()) {
            
            Date divorce = family.getDivorce();
            Date husbandDeath = family.getHusband().getDeath();
            Date wifeDeath = family.getWife().getDeath();

            if ((divorce != null && husbandDeath != null) && divorce.after(husbandDeath)) {
                System.out.printf("Error US06: Divorce of Family %s occurs after the death of %s (Individual %s)\n", family.getID(), family.getHusband().getName(), family.getHusband().getID());
                valid = false;
            }

            if ((divorce != null && wifeDeath != null) && divorce.after(wifeDeath)) {
                System.out.printf("Error US06: Divorce of Family %s occurs after the death of %s (Individual %s)\n", family.getID(), family.getWife().getName(), family.getWife().getID());
                valid = false;
            }
        }

        return valid;
    }

}
