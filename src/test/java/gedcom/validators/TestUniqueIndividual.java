package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import gedcom.builders.DateBuilder;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class TestUniqueIndividual {

    private Validator validator = new UniqueIndividuals(new DefaultValidator());

    @Test
    public void testValid() {
        String name1 = "John /Smith/";
        String name2 = "Bob /Smith/";

        Date birth1 = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date birth2 = DateBuilder.build(1, Calendar.JANUARY, 2001);

        Individual individual1 = Individual.builder().name(name1).birth(birth1).build();
        Individual individual2 = Individual.builder().name(name2).birth(birth2).build();

        GEDFile gedFile = GEDFile.builder().individuals(individual1, individual2).build();
        assertTrue(validator.check(gedFile));
    }

    @Test
    public void testInvalid() {
        String name = "John /Smith/";

        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);

        Individual individual1 = Individual.builder().name(name).birth(birth).build();
        Individual individual2 = Individual.builder().name(name).birth(birth).build();

        GEDFile gedFile = GEDFile.builder().individuals(individual1, individual2).build();
        assertFalse(validator.check(gedFile));
    }

}
