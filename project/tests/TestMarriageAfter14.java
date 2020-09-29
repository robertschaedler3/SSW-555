package project.tests;

import org.junit.Test;
import project.Validator;
import project.models.Family;
import project.models.GEDFile;
import project.models.Individual;
import project.validators.DefaultValidator;
import project.validators.MarriageAfter14;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestMarriageAfter14 {
    Validator validator = new DefaultValidator();
    Validator marriageAfter14Validator = new MarriageAfter14(validator);

    Individual husband1 = new Individual("Husband1");
    Individual wife1 = new Individual("Wife1");

    Individual husband2 = new Individual("Husband2");
    Individual wife2 = new Individual("Wife2");

    Individual[] individuals = {husband1, wife1, husband2, wife2};

    Family family1 = new Family("Family1");
    Family family2 = new Family("Family2");
    Family[] families = {family1, family2};

    public void setUpBaseDates() throws ParseException {
        husband1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));
        wife1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"));

        husband2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2005"));
        wife2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2006"));

        family1.setHusband(husband1.getID());
        family1.setWife(wife1.getID());

        family2.setHusband(husband2.getID());
        family2.setWife(wife2.getID());

        family1.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015"));
        family2.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
    }

    @Test
    public void checkMarriageBothValid() throws ParseException{
        setUpBaseDates();

        assertTrue(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage1InvalidHusband() throws ParseException{
        setUpBaseDates();

        husband1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage1InvalidWife() throws ParseException{
        setUpBaseDates();

        wife1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2011"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage1InvalidBoth() throws ParseException{
        setUpBaseDates();

        husband1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015"));
        wife1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2011"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage2InvalidHusband() throws ParseException{
        setUpBaseDates();

        husband2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("06/07/2009"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage2InvalidWife() throws ParseException{
        setUpBaseDates();

        wife2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("11/11/2018"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage2InvalidBoth() throws ParseException{
        setUpBaseDates();

        husband2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("06/07/2009"));
        wife2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("11/11/2018"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriageBothInvalid() throws ParseException{
        setUpBaseDates();

        husband1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015"));
        wife1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2011"));

        husband2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("06/07/2009"));
        wife2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("11/11/2018"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }
}
