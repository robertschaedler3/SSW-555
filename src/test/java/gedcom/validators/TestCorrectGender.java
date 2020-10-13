package gedcom.validators;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import gedcom.interfaces.Gender;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestCorrectGender {
    Validator validator = new DefaultValidator();
    Validator correctGenderValidator = new CorrectGender(validator);
    
    Individual male = new Individual("Male1");
    Individual female = new Individual("Female1");

    Individual[] individuals = {male, female};

    Family family1 = new Family("family1");

    Family[] families = {family1};

    public void setUpGender() {
        male.setGender(Gender.M);
        female.setGender(Gender.F);
    }

    @Test
    public void checkBaseFamily() {
        setUpGender();
        family1.setHusband(male);
        family1.setWife(female);

        assertTrue(correctGenderValidator.isValid(new GEDFile(individuals, families)));
    }

    @Test
    public void checkEmptyFamily() {
        assertTrue(correctGenderValidator.isValid(new GEDFile(individuals, families)));
    }

    @Test
    public void checkNoHusband() {
        setUpGender();
        family1.setWife(female);

        assertTrue(correctGenderValidator.isValid(new GEDFile(individuals, families)));
    }

    @Test
    public void checkNoWife() {
        setUpGender();
        family1.setWife(female);

        assertTrue(correctGenderValidator.isValid(new GEDFile(individuals, families)));
    }

    @Test
    public void checkTwoM() {
        setUpGender();
        family1.setHusband(male);
        family1.setWife(male);

        assertFalse(correctGenderValidator.isValid(new GEDFile(individuals, families)));
    }

    @Test
    public void checkTwoF() {
        setUpGender();
        family1.setHusband(female);
        family1.setWife(female);

        assertFalse(correctGenderValidator.isValid(new GEDFile(individuals, families)));
    }

    @Test
    public void checkSwapped() {
        setUpGender();
        family1.setHusband(female);
        family1.setWife(male);

        assertFalse(correctGenderValidator.isValid(new GEDFile(individuals, families)));
    }
}
