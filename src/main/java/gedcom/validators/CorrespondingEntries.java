package gedcom.validators;

import gedcom.models.Family;
import gedcom.models.GEDFile;

import java.util.Date;

public class CorrespondingEntries extends Validator {

    public CorrespondingEntries(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        for (Family family : gedFile.getFamilies()) {
            Individual husband = family.getHusband();
            Individual wife = family.getWife();

            if(wife.getSpouses().contains(husband)
            && husband.getSpouses().contains(wife)
            && wife.getChildren()
        }

        return valid;
    }

}