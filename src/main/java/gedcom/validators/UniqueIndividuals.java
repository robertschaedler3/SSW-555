package gedcom.validators;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class UniqueIndividuals extends Validator {

    public UniqueIndividuals(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;
        Map<String, List<Date>> nameDateMap = new HashMap<>();

        for (Individual individual : gedFile.getIndividuals()) {
            String name = individual.getName();
            Date birth = individual.getBirthday();
            List<Date> dates = null;
            if (birth != null) {
                if ((dates = nameDateMap.get(name)) != null && dates.contains(birth)) {
                    System.out.println("Anomaly US23: Multiple individuals with the same name have the same birth date.");
                    valid = false;
                } 
                nameDateMap.computeIfAbsent(name, k -> new ArrayList<Date>()).add(birth);
            } 
        }
        
        return valid;
    }

}
