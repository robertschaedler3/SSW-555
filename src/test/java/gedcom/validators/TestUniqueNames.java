package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import gedcom.builders.FamilyBuilder;
import gedcom.builders.GEDFileBuilder;
import gedcom.builders.IndividualBuilder;
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

        Individual husband = new IndividualBuilder().build();
        Individual wife = new IndividualBuilder().build();
        Individual child1 = new IndividualBuilder().withName(childName1).withBirth(childBirthday1).build();
        Individual child2 = new IndividualBuilder().withName(childName2).withBirth(childBirthday2).build();

        Family family1 = new FamilyBuilder().withHusband(husband).withWife(wife).withChild(child1).withChild(child2).build();

        List<Individual> individuals = new ArrayList<>(Arrays.asList(husband, wife, child1, child2));
        List<Family> families = new ArrayList<>(Arrays.asList(family1));
        GEDFile gedFile = new GEDFileBuilder().withIndividuals(individuals).withFamilies(families).build();

        assertTrue(uniqueNames.isValid(gedFile));
    }

    @Test
    public void checkSameName() {
        Date childBirthday1 = DateBuilder.build(2, Calendar.JANUARY, 1990);
        Date childBirthday2 = DateBuilder.build(2, Calendar.JANUARY, 1994);
        String childName1 = "Terry";
        String childName2 = "Terry";

        Individual husband = new IndividualBuilder().build();
        Individual wife = new IndividualBuilder().build();
        Individual child1 = new IndividualBuilder().withName(childName1).withBirth(childBirthday1).build();
        Individual child2 = new IndividualBuilder().withName(childName2).withBirth(childBirthday2).build();

        Family family1 = new FamilyBuilder().withHusband(husband).withWife(wife).withChild(child1).withChild(child2).build();

        List<Individual> individuals = new ArrayList<>(Arrays.asList(husband, wife, child1, child2));
        List<Family> families = new ArrayList<>(Arrays.asList(family1));
        GEDFile gedFile = new GEDFileBuilder().withIndividuals(individuals).withFamilies(families).build();

        assertTrue(uniqueNames.isValid(gedFile));
    }

    @Test
    public void checkSameNameBirthday() {
        Date childBirthday1 = DateBuilder.build(2, Calendar.JANUARY, 1990);
        Date childBirthday2 = DateBuilder.build(2, Calendar.JANUARY, 1990);
        String childName1 = "Terry";
        String childName2 = "Terry";

        Individual husband = new IndividualBuilder().build();
        Individual wife = new IndividualBuilder().build();
        Individual child1 = new IndividualBuilder().withName(childName1).withBirth(childBirthday1).build();
        Individual child2 = new IndividualBuilder().withName(childName2).withBirth(childBirthday2).build();

        Family family1 = new FamilyBuilder().withHusband(husband).withWife(wife).withChild(child1).withChild(child2).build();

        List<Individual> individuals = new ArrayList<>(Arrays.asList(husband, wife, child1, child2));
        List<Family> families = new ArrayList<>(Arrays.asList(family1));
        GEDFile gedFile = new GEDFileBuilder().withIndividuals(individuals).withFamilies(families).build();

        assertFalse(uniqueNames.isValid(gedFile));
    }
}
