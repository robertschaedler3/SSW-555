package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import gedcom.builders.DateBuilder;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class TestMultipleBirths {

    private Validator validator = new MultipleBirths(new DefaultValidator());

    @Test
    public void testValid() {
        Family family = Family.builder().build();

        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        List<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < Family.MAX_MULTIPLE_BIRTH; i++) {
            Individual child = Individual.builder().birth(birth).build();
            individuals.add(child);
            family.addChild(child);
        }

        GEDFile gedFile = GEDFile.builder().individuals(individuals).family(family).build();
        assertTrue(validator.isValid(gedFile));
    }

    @Test
    public void testInvalid() {
        Family family = Family.builder().build();

        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        List<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < Family.MAX_MULTIPLE_BIRTH + 1; i++) {
            Individual child = Individual.builder().birth(birth).build();
            individuals.add(child);
            family.addChild(child);
        }

        GEDFile gedFile = GEDFile.builder().individuals(individuals).family(family).build();
        assertFalse(validator.isValid(gedFile));
    }

}
