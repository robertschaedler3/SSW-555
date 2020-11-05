package gedcom.validators;

import java.util.Calendar;
import java.util.List;

import gedcom.logging.Error;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class ValidBirth extends Validator {

    public ValidBirth(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {

        for (Family family : gedFile.getFamilies()) {

            List<Individual> children = family.getChildren();
            
            for (int i = 0; i < children.size(); i++) {

                Individual child = children.get(i);
                Individual mother = family.getWife();
                Individual father = family.getHusband();

                if (child.getBirthday() != null) {
                    if (family.getMarriage() != null && child.getBirthday().before(family.getMarriage())) {
                        LOGGER.anomaly(Error.BIRTH_BEFORE_MARRIAGE_OF_PARENTS, family, child);
                        valid = false;
                    }
                    if (mother != null && mother.getDeath() != null && child.getBirthday().after(mother.getDeath())) {
                        LOGGER.error(Error.DEATH_OF_PARENTS_BEFORE_BIRTH, family, mother, child);
                        valid = false;
                    }
                    if (father != null && father.getDeath() != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(father.getDeath());
                        calendar.add(Calendar.MONTH, 9);
                        if (child.getBirthday().after(calendar.getTime())) {
                            LOGGER.error(Error.DEATH_BEFORE_BIRTH, family, father, child);
                            valid = false;
                        }
                    }
                    for (int j = i + 1; j < children.size(); j++) {
                        Individual sibling = children.get(j);
                        if (sibling.getBirthday() != null) {
                            long diff = Math.abs(child.getBirthday().getTime() - sibling.getBirthday().getTime());
                            long diffInDays = (diff / (1000 * 60 * 60 * 24)) % 365;
                            if (diffInDays > 2 && diffInDays < (30 * 8)) {
                                LOGGER.anomaly(Error.SIBLINGS_SPACING_TOO_CLOSE, child, sibling);
                                valid = false;
                            }
                        }
                    }
                }
            }
        }

        return valid;
    }
}
