package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;

public class TestMarriageAfter14 {
    Validator validator = new DefaultValidator();
    Validator marriageAfter14Validator = new MarriageAfter14(validator);

    Individual husband1 = new Individual("Husband1");
    Individual wife1 = new Individual("Wife1");

    Individual husband2 = new Individual("Husband2");
    Individual wife2 = new Individual("Wife2");

    Individual[] individuals = { husband1, wife1, husband2, wife2 };

    Family family1 = new Family("Family1");
    Family family2 = new Family("Family2");
    Family[] families = { family1, family2 };

    public void setUpBaseDates() throws ParseException {
        husband1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));
        wife1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"));

        husband2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2005"));
        wife2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2006"));

        family1.setHusband(husband1);
        family1.setWife(wife1);

        family2.setHusband(husband2);
        family2.setWife(wife2);

        family1.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015"));
        family2.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
    }

    @Test
    public void checkMarriageBothValid() throws ParseException {
        setUpBaseDates();

        assertTrue(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage1InvalidHusband() throws ParseException {
        setUpBaseDates();

        husband1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage1InvalidWife() throws ParseException {
        setUpBaseDates();

        wife1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2011"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage1InvalidBoth() throws ParseException {
        setUpBaseDates();

        husband1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015"));
        wife1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2011"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage2InvalidHusband() throws ParseException {
        setUpBaseDates();

        husband2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("06/07/2009"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage2InvalidWife() throws ParseException {
        setUpBaseDates();

        wife2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("11/11/2018"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriage2InvalidBoth() throws ParseException {
        setUpBaseDates();

        husband2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("06/07/2009"));
        wife2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("11/11/2018"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkMarriageBothInvalid() throws ParseException {
        setUpBaseDates();

        husband1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015"));
        wife1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("04/03/2011"));

        husband2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("06/07/2009"));
        wife2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("11/11/2018"));

        assertFalse(marriageAfter14Validator.isValid((new GEDFile(individuals, families))));
    }
}
