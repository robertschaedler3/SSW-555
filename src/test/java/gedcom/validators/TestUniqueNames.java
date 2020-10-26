package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TestUniqueNames {
    Validator validator = new DefaultValidator();
    Validator uniqueNames = new UniqueNameBirthdays(validator);

    @Test
    public void checkDifferentName() throws ParseException {
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
    public void checkSameName() throws ParseException {
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
    public void checkSameNameBirthday() throws ParseException {
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

        assertTrue(uniqueNames.isValid(gedFile));
    }
}
