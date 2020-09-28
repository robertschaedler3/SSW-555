package project.tests;

import org.junit.Test;
import project.Validator;
import project.models.Family;
import project.models.GEDFile;
import project.models.Individual;
import project.validators.DatesBeforeCurrentDate;
import project.validators.DefaultValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestDatesBeforeCurrentDate {
    Validator validator = new DefaultValidator();
    Validator datesBeforeCurrentDateValidator = new DatesBeforeCurrentDate(validator);

    Individual individual1 = new Individual("Individual1");
    Individual individual2 = new Individual("Individual2");
    Individual individual3 = new Individual("Individual3");

    Individual[] individuals = {individual1, individual2, individual3};

    Family family1 = new Family("Family1");
    Family family2 = new Family("Family2");
    Family family3 = new Family("Family3");
    Family[] families = {family1, family2, family3};

    public void setUpBaseDates() throws ParseException {
        individual1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
        individual2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1999"));
        individual3.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1989"));

        family1.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
        family2.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1999"));
        family3.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1989"));
    }


    @Test
    public void checkBirthValid() throws ParseException {
        setUpBaseDates();

        assertTrue(datesBeforeCurrentDateValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkBirthInvalid() throws ParseException {
        setUpBaseDates();

        individual1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2021"));
        individual2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2030"));

        assertFalse(datesBeforeCurrentDateValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkDeathValid() throws ParseException {
        setUpBaseDates();

        individual1.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
        individual2.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1999"));
        individual3.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1989"));

        assertTrue(datesBeforeCurrentDateValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkDeathInvalid() throws ParseException {
        setUpBaseDates();

        individual1.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2021"));
        individual2.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2030"));
        individual3.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1989"));

        assertFalse(datesBeforeCurrentDateValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriageValid() throws ParseException {
        setUpBaseDates();

        assertTrue(datesBeforeCurrentDateValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriageInvalid() throws ParseException {
        setUpBaseDates();

        family1.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2021"));
        family2.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2030"));

        assertFalse(datesBeforeCurrentDateValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkDivorceValid() throws ParseException {
        setUpBaseDates();

        family1.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
        family2.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1999"));
        family3.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1989"));

        assertTrue(datesBeforeCurrentDateValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkDivorceInvalid() throws ParseException {
        setUpBaseDates();

        family1.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2021"));
        family2.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2030"));
        family3.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1989"));

        assertFalse(datesBeforeCurrentDateValidator.isValid((new GEDFile(individuals, families))));
    }
}
