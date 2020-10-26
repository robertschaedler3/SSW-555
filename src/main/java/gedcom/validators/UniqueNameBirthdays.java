package gedcom.validators;

import gedcom.models.GEDFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import gedcom.models.Family;
import gedcom.models.Individual;

public class UniqueNameBirthdays extends Validator {

    public UniqueNameBirthdays(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;
        List<Map<String, Date>> childrenWithoutSameName = new ArrayList<>();

        for (Family family : gedFile.getFamilies()) {
            for (Individual child : family.getChildren()) {
                String childName = child.getName();
                Date childBirthday = child.getBirthday();

                for(Map<String, Date> checkedChildren : childrenWithoutSameName){
                    if (checkedChildren.containsKey(childName)){
                        if(childBirthday.equals(checkedChildren.get(childName))) {
                            System.out.printf("Anomaly US25: More than one child with the same name in %s\n",
                                    family.getID());
                            valid = false;
                        }
                    }
                }

                Map<String, Date> checkedChild = new HashMap<>();
                checkedChild.put(childName, childBirthday);

                childrenWithoutSameName.add(checkedChild);

            }
        }

        return valid;
    }

}
