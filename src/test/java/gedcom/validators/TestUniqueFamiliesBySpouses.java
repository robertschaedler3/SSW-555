package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import gedcom.builders.DateBuilder;
import gedcom.builders.FamilyBuilder;
import gedcom.builders.GEDFileBuilder;
import gedcom.builders.IndividualBuilder;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class TestUniqueFamiliesBySpouses {

    private Validator validator = new UniqueFamiliesBySpouses(new DefaultValidator());
    
    @Test
    public void testValid() {
        Individual husband1 = new IndividualBuilder().male().build();
        Individual wife1 = new IndividualBuilder().female().build();
        Date marriage1 = DateBuilder.build(1, Calendar.JANUARY, 2000);
        
        Individual husband2 = new IndividualBuilder().male().build();
        Individual wife2 = new IndividualBuilder().female().build();
        Date marriage2 = DateBuilder.build(2, Calendar.JANUARY, 2000);
        
        Family family1 = new FamilyBuilder().withHusband(husband1).withWife(wife1).withMarriage(marriage1).build();
        Family family2 = new FamilyBuilder().withHusband(husband2).withWife(wife2).withMarriage(marriage2).build();

        GEDFile gedFile = new GEDFileBuilder().withIndividuals(husband1, wife1, husband2, wife2).withFamilies(family1, family2).build();
        assertTrue(validator.isValid(gedFile));
    }
    
    @Test
    public void testInvalid() {
        Individual husband = new IndividualBuilder().male().build();
        Individual wife = new IndividualBuilder().female().build();
        Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2000);

        Family family1 = new FamilyBuilder().withHusband(husband).withWife(wife).withMarriage(marriage).build();
        Family family2 = new FamilyBuilder().withHusband(husband).withWife(wife).withMarriage(marriage).build();

        GEDFile gedFile = new GEDFileBuilder().withIndividuals(husband, wife).withFamilies(family1, family2).build();
        assertFalse(validator.isValid(gedFile));
    }

}
