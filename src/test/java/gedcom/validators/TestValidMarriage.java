package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import gedcom.builders.DateBuilder;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class TestValidMarriage {

    private Validator validator = new ValidMarriage(new DefaultValidator());

    private Date addYears(Date d, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    @Test
    public void testValidMarriageAgeAfterBirth() {
        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date marriage = addYears(birth, Family.MIN_MARRIAGE_AGE + 1);

        Individual individual = Individual.builder().birth(birth).build();

        Family family = Family.builder().husband(individual).marriage(marriage).build();
        GEDFile gedFile = GEDFile.builder().family(family).individual(individual).build();

        assertTrue(validator.isValid(gedFile));
    }

    @Test
    public void testInvalidMarriageAge() {
        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date marriage = addYears(birth, Family.MIN_MARRIAGE_AGE - 1);

        Individual individual = Individual.builder().birth(birth).build();

        Family family = Family.builder().husband(individual).marriage(marriage).build();
        GEDFile gedFile = GEDFile.builder().family(family).individual(individual).build();

        assertFalse(validator.isValid(gedFile));
    }

    @Test
    public void testInvalidMarriageAfterBirth() {
        Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date birth = addYears(marriage, 1); // Birth 1 year after marriage

        Individual individual = Individual.builder().birth(birth).build();

        Family family = Family.builder().husband(individual).marriage(marriage).build();
        GEDFile gedFile = GEDFile.builder().family(family).individual(individual).build();

        assertFalse(validator.isValid(gedFile));
    }

    @Test
    public void testValidMarriageBeforeDeath() {
        Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date death = addYears(marriage, 1); // Death 1 year after marriage

        Individual individual = Individual.builder().death(death).build();

        Family family = Family.builder().husband(individual).marriage(marriage).build();
        GEDFile gedFile = GEDFile.builder().family(family).individual(individual).build();

        assertTrue(validator.isValid(gedFile));
    }

    @Test
    public void testInvalidMarriageBeforeDeath() {
        Date death = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date marriage = addYears(death, 1); // Marriage 1 year after death

        Individual individual = Individual.builder().death(death).build();

        Family family = Family.builder().husband(individual).marriage(marriage).build();
        GEDFile gedFile = GEDFile.builder().family(family).individual(individual).build();

        assertFalse(validator.isValid(gedFile));
    }

    @Test
    public void testValidDivorceBeforeDeath() {
        Date divorce = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date death = addYears(divorce, 1); // Death 1 year after divorce

        Individual individual = Individual.builder().death(death).build();

        Family family = Family.builder().husband(individual).divorce(divorce).build();
        GEDFile gedFile = GEDFile.builder().family(family).individual(individual).build();

        assertTrue(validator.isValid(gedFile));
    }

    @Test
    public void testInvalidDivorceBeforeDeath() {
        Date death = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date divorce = addYears(death, 1); // Divorce 1 year after death

        Individual individual = Individual.builder().death(death).build();

        Family family = Family.builder().husband(individual).divorce(divorce).build();
        GEDFile gedFile = GEDFile.builder().family(family).individual(individual).build();

        assertFalse(validator.isValid(gedFile));
    }

}
