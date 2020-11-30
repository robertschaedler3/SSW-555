package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import gedcom.builders.DateBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUniqueNames {
    Validator validator = new DefaultValidator();
    Validator uniqueNames = new UniqueNameBirthdays(validator);

    @Test
    public void checkDifferentName() {
        Date childBirthday1 = DateBuilder.build(2, Calendar.JANUARY, 1990);
        Date childBirthday2 = DateBuilder.build(2, Calendar.JANUARY, 1994);
        String childName1 = "Terry";
        String childName2 = "Bob";

        Individual husband = Individual.builder().build();
        Individual wife = Individual.builder().build();
        Individual child1 = Individual.builder().name(childName1).birth(childBirthday1).build();
        Individual child2 = Individual.builder().name(childName2).birth(childBirthday2).build();

        Family family1 = Family.builder().husband(husband).wife(wife).child(child1).child(child2).build();

        List<Individual> individuals = new ArrayList<>(Arrays.asList(husband, wife, child1, child2));
        List<Family> families = new ArrayList<>(Arrays.asList(family1));
        GEDFile gedFile = GEDFile.builder().individuals(individuals).families(families).build();

        assertTrue(uniqueNames.isValid(gedFile));
    }

    @Test
    public void checkSameName() {
        Date childBirthday1 = DateBuilder.build(2, Calendar.JANUARY, 1990);
        Date childBirthday2 = DateBuilder.build(2, Calendar.JANUARY, 1994);
        String childName1 = "Terry";
        String childName2 = "Terry";

        Individual husband = Individual.builder().build();
        Individual wife = Individual.builder().build();
        Individual child1 = Individual.builder().name(childName1).birth(childBirthday1).build();
        Individual child2 = Individual.builder().name(childName2).birth(childBirthday2).build();

        Family family1 = Family.builder().husband(husband).wife(wife).child(child1).child(child2).build();

        List<Individual> individuals = new ArrayList<>(Arrays.asList(husband, wife, child1, child2));
        List<Family> families = new ArrayList<>(Arrays.asList(family1));
        GEDFile gedFile = GEDFile.builder().individuals(individuals).families(families).build();

        assertTrue(uniqueNames.isValid(gedFile));
    }

    @Test
    public void checkSameNameBirthday() {
        Date childBirthday1 = DateBuilder.build(2, Calendar.JANUARY, 1990);
        Date childBirthday2 = DateBuilder.build(2, Calendar.JANUARY, 1990);
        String childName1 = "Terry";
        String childName2 = "Terry";

        Individual husband = Individual.builder().build();
        Individual wife = Individual.builder().build();
        Individual child1 = Individual.builder().name(childName1).birth(childBirthday1).build();
        Individual child2 = Individual.builder().name(childName2).birth(childBirthday2).build();

        Family family1 = Family.builder().husband(husband).wife(wife).child(child1).child(child2).build();

        List<Individual> individuals = new ArrayList<>(Arrays.asList(husband, wife, child1, child2));
        List<Family> families = new ArrayList<>(Arrays.asList(family1));
        GEDFile gedFile = GEDFile.builder().individuals(individuals).families(families).build();

        assertFalse(uniqueNames.isValid(gedFile));
    }
}
