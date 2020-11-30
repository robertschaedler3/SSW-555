package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import gedcom.builders.DateBuilder;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class TestNoBigamy {

    private Validator validator = new NoBigamy(new DefaultValidator());

    @Test
    public void testValid() {
        Individual husband = Individual.builder().build();
        Individual wife1 = Individual.builder().build();
        Individual wife2 = Individual.builder().build();

        Date marriage1 = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date divorce1 = DateBuilder.build(1, Calendar.JANUARY, 2001);
        Family family1 = Family.builder().spouses(husband, wife1).marriage(marriage1).divorce(divorce1).build();
        
        Date marriage2 = DateBuilder.build(1, Calendar.JANUARY, 2002);
        Date divorce2 = DateBuilder.build(1, Calendar.JANUARY, 2003);
        Family family2 = Family.builder().spouses(husband, wife1).marriage(marriage2).divorce(divorce2).build();
        
        GEDFile gedFile = GEDFile.builder().families(family1, family2).individuals(husband, wife1, wife2).build();
        assertTrue(validator.isValid(gedFile));
    }

    @Test
    public void testInvalid() {
        Individual husband = Individual.builder().build();
        Individual wife1 = Individual.builder().build();
        Individual wife2 = Individual.builder().build();
        
        Date marriage1 = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date divorce1 = DateBuilder.build(1, Calendar.JANUARY, 2001);
        Family family1 = Family.builder().spouses(husband, wife1).marriage(marriage1).divorce(divorce1).build();

        Date marriage2 = DateBuilder.build(1, Calendar.JUNE, 2000); // Overlaps with marriage1/divorce1
        Date divorce2 = DateBuilder.build(1, Calendar.JANUARY, 2002);
        Family family2 = Family.builder().spouses(husband, wife1).marriage(marriage2).divorce(divorce2).build();

        GEDFile gedFile = GEDFile.builder().families(family1, family2).individuals(husband, wife1, wife2).build();
        assertFalse(validator.isValid(gedFile));
    }

}
