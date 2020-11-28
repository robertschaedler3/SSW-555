package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import gedcom.builders.DateBuilder;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class TestUniqueFamiliesBySpouses {

    private Validator validator = new UniqueFamiliesBySpouses(new DefaultValidator());
    
    @Test
    public void testValid() {
        Individual husband1 = Individual.builder().male().build();
        Individual wife1 = Individual.builder().female().build();
        Date marriage1 = DateBuilder.build(1, Calendar.JANUARY, 2000);
        
        Individual husband2 = Individual.builder().male().build();
        Individual wife2 = Individual.builder().female().build();
        Date marriage2 = DateBuilder.build(2, Calendar.JANUARY, 2000);
        
        Family family1 = Family.builder().husband(husband1).wife(wife1).marriage(marriage1).build();
        Family family2 = Family.builder().husband(husband2).wife(wife2).marriage(marriage2).build();

        GEDFile gedFile = GEDFile.builder().individuals(husband1, wife1, husband2, wife2).families(family1, family2)
                .build();
        assertTrue(validator.isValid(gedFile));
    }
    
    @Test
    public void testInvalid() {
        Individual husband = Individual.builder().male().build();
        Individual wife = Individual.builder().female().build();
        Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2000);

        Family family1 = Family.builder().husband(husband).wife(wife).marriage(marriage).build();
        Family family2 = Family.builder().husband(husband).wife(wife).marriage(marriage).build();

        GEDFile gedFile = GEDFile.builder().individuals(husband, wife).families(family1, family2).build();
        assertFalse(validator.isValid(gedFile));
    }

}
