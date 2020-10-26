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

    Individual husband1 = new Individual("Husband1");
    Individual wife1 = new Individual("Wife1");

    Individual child1 = new Individual("Child1");
    Individual child2 = new Individual("Child2");

    Individual[] individuals = { husband1, wife1, child1, child2 };

    Family family1 = new Family("Family1");
    Family[] families = { family1 };

    public void setBaseFamilyInfo() throws ParseException {
        child1.setName("terry");
        child2.setName("benard");

        child1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));
        child2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1994"));

        family1.setHusband(husband1);
        family1.setWife(wife1);

        family1.addChild(child1);
        family1.addChild(child2);
    }

    @Test
    public void checkDifferentName() throws ParseException {
        setBaseFamilyInfo();

        assertTrue(uniqueNames.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkSameName() throws ParseException {
        setBaseFamilyInfo();

        child2.setName("terry");

        assertTrue(uniqueNames.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkSameNameBirthday() throws ParseException {
        setBaseFamilyInfo();

        child2.setName("terry");
        child2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));

        assertFalse(uniqueNames.isValid((new GEDFile(individuals, families))));
    }
}
