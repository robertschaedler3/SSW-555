package gedcom.validators;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

public class TestDivorceBeforeDeath {
    Validator validator = new DefaultValidator();
    Validator divorceBeforeDeathValidator = new DivorceBeforeDeath(validator);

    Individual husband1 = new Individual("Husband1");
    Individual wife1 = new Individual("Wife1");

    Individual husband2 = new Individual("Husband2");
    Individual wife2 = new Individual("Wife2");

    Individual[] individuals = { husband1, wife1, husband2, wife2 };

    Family family1 = new Family("Family1");
    Family family2 = new Family("Family2");
    Family[] families = { family1, family2 };

    public void setUpBaseDates() throws ParseException {
        husband1.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));
        wife1.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"));

        husband2.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2005"));
        wife2.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2006"));

        family1.setHusband(husband1);
        family1.setWife(wife1);

        family2.setHusband(husband2);
        family2.setWife(wife2);

    }

    @Test
    public void checkDivorceBothValid() throws ParseException {
        setUpBaseDates();

        family1.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));
        family2.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"));

        assertTrue(divorceBeforeDeathValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkDivorce1InvalidHusband() throws ParseException {
        setUpBaseDates();

        husband1.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));

        family1.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));

        assertFalse(divorceBeforeDeathValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkDivorce1InvalidWife() throws ParseException {
        setUpBaseDates();

        wife1.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));

        family1.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));

        assertFalse(divorceBeforeDeathValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkDivorce1InvalidBoth() throws ParseException {
        setUpBaseDates();

        husband1.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));
        wife1.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));
        family1.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));

        assertFalse(divorceBeforeDeathValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkDivorce2InvalidHusband() throws ParseException {
        setUpBaseDates();

        husband2.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));
        family2.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));

        assertFalse(divorceBeforeDeathValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkDivorce2InvalidWife() throws ParseException {
        setUpBaseDates();

        wife2.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));

        family2.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));

        assertFalse(divorceBeforeDeathValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkDivorce2InvalidBoth() throws ParseException {
        setUpBaseDates();

        husband2.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));
        wife2.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));
        family2.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));

        assertFalse(divorceBeforeDeathValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkDivorceBothInvalid() throws ParseException {
        setUpBaseDates();

        husband1.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));
        wife1.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));
        family1.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));

        husband2.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));
        wife2.setDeath(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1990"));
        family2.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));

        assertFalse(divorceBeforeDeathValidator.isValid((new GEDFile(individuals, families))));
    }
}
