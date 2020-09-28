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

            Calendar marriage = Calendar.getInstance();
            marriage.setTime(family.getMarriage());

            Individual husband = individuals.get(family.getHusband());
            Calendar husbandBirth = Calendar.getInstance();
            husbandBirth.setTime(husband.getBirthday());
            husbandBirth.add(Calendar.YEAR, 14);

            Individual wife = individuals.get(family.getWife());
            Calendar wifeBirth = Calendar.getInstance();
            wifeBirth.setTime(wife.getBirthday());
            wifeBirth.add(Calendar.YEAR, 14);

            if(marriage.compareTo(husbandBirth) < 0){
                System.out.printf("Individual %s was married below the age of 14", husband.getID());
                valid = false;
            }

            if(marriage.compareTo(wifeBirth) < 0){
                System.out.printf("Individual %s was married below the age of 14", wife.getID());
                valid = false;
            }
        }
        return valid;
    }
}
