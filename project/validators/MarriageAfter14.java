package project.validators;

import project.Validator;
import project.models.Family;
import project.models.GEDFile;
import project.models.Individual;

import java.util.Calendar;
import java.util.Map;

public class MarriageAfter14 extends Validator {

    public MarriageAfter14(Validator validator){
        super(validator);
    }

    protected boolean check(GEDFile gedFile){
        boolean valid = true;
        Map<String, Individual> individuals = gedFile.getIndividuals();

        for(Map.Entry<String, Family> entry : gedFile.getFamilies().entrySet()){
            Family family = entry.getValue();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(family.getMarriage());

            int marriage = calendar.get(Calendar.YEAR);

            Individual husband = individuals.get(family.getHusband());
            calendar.setTime(husband.getBirthday());
            int husbandBirth = calendar.get(Calendar.YEAR);

            Individual wife = individuals.get(family.getWife());
            calendar.setTime(wife.getBirthday());
            int wifeBirth = calendar.get(Calendar.YEAR);

            if(marriage - husbandBirth < 14){
                System.out.printf("Individual %s was married below the age of 14", husband.getID());
                valid = false;
            }

            if(marriage - wifeBirth < 14){
                System.out.printf("Individual %s was married below the age of 14", wife.getID());
                valid = false;
            }
        }
        return valid;
    }
}
