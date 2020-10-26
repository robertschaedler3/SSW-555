package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import gedcom.builders.DateBuilder;
import gedcom.builders.FamilyBuilder;
import gedcom.builders.GEDFileBuilder;
import gedcom.builders.IndividualBuilder;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class TestMultipleBirths {

    private Validator validator = new MultipleBirths(new DefaultValidator());

    @Test
    public void testValid() {
        Family family = new FamilyBuilder().build();

        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        List<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < Family.MAX_MULTIPLE_BIRTH; i++) {
            Individual child = new IndividualBuilder().withBirth(birth).build();
            individuals.add(child);
            family.addChild(child);
        }

        GEDFile gedFile = new GEDFileBuilder().withIndividuals(individuals).withFamily(family).build();
        assertTrue(validator.isValid(gedFile));
    }

    @Test
    public void testInvalid() {
        Family family = new FamilyBuilder().build();

        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        List<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < Family.MAX_MULTIPLE_BIRTH + 1; i++) {
            Individual child = new IndividualBuilder().withBirth(birth).build();
            individuals.add(child);
            family.addChild(child);
        }

        GEDFile gedFile = new GEDFileBuilder().withIndividuals(individuals).withFamily(family).build();
        assertFalse(validator.isValid(gedFile));
    }

}
