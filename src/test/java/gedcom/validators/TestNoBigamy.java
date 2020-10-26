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

public class TestNoBigamy {

    private Validator validator = new NoBigamy(new DefaultValidator());

    @Test
    public void testValid() {
        Individual husband = new IndividualBuilder().build();
        Individual wife1 = new IndividualBuilder().build();
        Individual wife2 = new IndividualBuilder().build();

        Date marriage1 = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date divorce1 = DateBuilder.build(1, Calendar.JANUARY, 2001);
        Family family1 = new FamilyBuilder().withSpouses(husband, wife1).withMarriage(marriage1).withDivorce(divorce1).build();
        
        Date marriage2 = DateBuilder.build(1, Calendar.JANUARY, 2002);
        Date divorce2 = DateBuilder.build(1, Calendar.JANUARY, 2003);
        Family family2 = new FamilyBuilder().withSpouses(husband, wife1).withMarriage(marriage2).withDivorce(divorce2).build();
        
        GEDFile gedFile = new GEDFileBuilder().withFamilies(family1, family2).withIndividuals(husband, wife1, wife2).build();
        assertTrue(validator.isValid(gedFile));
    }

    @Test
    public void testInvalid() {
        Individual husband = new IndividualBuilder().build();
        Individual wife1 = new IndividualBuilder().build();
        Individual wife2 = new IndividualBuilder().build();
        
        Date marriage1 = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date divorce1 = DateBuilder.build(1, Calendar.JANUARY, 2001);
        Family family1 = new FamilyBuilder().withSpouses(husband, wife1).withMarriage(marriage1).withDivorce(divorce1).build();

        Date marriage2 = DateBuilder.build(1, Calendar.JUNE, 2000); // Overlaps with marriage1/divorce1
        Date divorce2 = DateBuilder.build(1, Calendar.JANUARY, 2002);
        Family family2 = new FamilyBuilder().withSpouses(husband, wife1).withMarriage(marriage2).withDivorce(divorce2).build();

        GEDFile gedFile = new GEDFileBuilder().withFamilies(family1, family2).withIndividuals(husband, wife1, wife2).build();
        assertFalse(validator.isValid(gedFile));
    }

}
