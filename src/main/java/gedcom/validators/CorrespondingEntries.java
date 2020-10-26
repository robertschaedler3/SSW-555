package gedcom.validators;

import gedcom.models.GEDFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import gedcom.models.Family;
import gedcom.models.Individual;

public class CorrespondingEntries extends Validator {

    public CorrespondingEntries(Validator validator) {
        super(validator);
    }

    protected boolean check(GEDFile gedFile) {
        boolean valid = true;

        for (Family family : gedFile.getFamilies()) {
            Individual familyFather = family.getHusband();
            Individual familyWife = family.getWife();
            List<Individual> familyChildren = family.getChildren();

            if( gedFile.getIndividual(familyFather.getID()) != null){
                System.out.printf("Error US26: %s does not have a corresponding entry\n", familyFather.getName());
                valid = false;
            }
            if( gedFile.getIndividual(familyWife.getID()) != null ){
                System.out.printf("Error US26: %s does not have a corresponding entry\n", familyWife.getName());
                valid = false;
            }

            for(Individual child : familyChildren){
                if( gedFile.getIndividual(child.getID()) != null ){
                    System.out.printf("Error US26: %s does not have a corresponding entry\n", child.getName());
                    valid = false;
                }
            }
        }

        return valid;
    }

}
