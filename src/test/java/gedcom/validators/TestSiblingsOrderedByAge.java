package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;

public class TestSiblingsOrderedByAge {
    Validator validator = new DefaultValidator();
    Validator siblingsOrderedByAgeValidator = new SiblingsOrderedByAge(validator);

    Individual individual1 = new Individual("Individual1");
    Individual individual2 = new Individual("Individual2");
    Individual individual3 = new Individual("Individual3");
    Individual individual4 = new Individual("Individual4");

    Individual[] individuals = { individual1, individual2, individual3 };

    Family family1 = new Family("Family1");

    Family[] families = { family1 };

    public void setUpBirthdays() throws ParseException {
        individual1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1998"));
        individual2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1999"));
        individual3.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"));
        individual4.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"));
    }

    @Test
    public void checkEmptyFamily() {
        assertTrue(siblingsOrderedByAgeValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkOneChild() throws ParseException {
        setUpBirthdays();

        family1.addChild(individual1);

        assertTrue(siblingsOrderedByAgeValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkTwoCorrect() throws ParseException {
        setUpBirthdays();

        family1.addChild(individual1);
        family1.addChild(individual2);

        assertTrue(siblingsOrderedByAgeValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkTwoIncorrect() throws ParseException {
        setUpBirthdays();

        family1.addChild(individual2);
        family1.addChild(individual1);

        assertFalse(siblingsOrderedByAgeValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkTwoSameBirthday() throws ParseException {
        setUpBirthdays();

        family1.addChild(individual3);
        family1.addChild(individual4);

        assertTrue(siblingsOrderedByAgeValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkThreeCorrect() throws ParseException {
        setUpBirthdays();

        family1.addChild(individual1);
        family1.addChild(individual2);
        family1.addChild(individual3);

        assertTrue(siblingsOrderedByAgeValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkThreeIncorrect() throws ParseException {
        setUpBirthdays();

        family1.addChild(individual1);
        family1.addChild(individual3);
        family1.addChild(individual2);

        assertFalse(siblingsOrderedByAgeValidator.isValid((new GEDFile(individuals, families))));
    }

}