package gedcom;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import gedcom.interfaces.Gender;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import gedcom.validators.DefaultValidator;
import gedcom.validators.MarriageBeforeDivorce;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestMarriageBeforeDivorce {

	Validator validator = new DefaultValidator();
	Validator MarriageBeforeDivorce = new MarriageBeforeDivorce(validator);

	@Test
	public void testMarriageBeforeDivorceValidator() {
		try {
			// test at max age difference
			Individual father1 = new Individual("Father1");
			father1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1940"));
			father1.setGender(Gender.M);

			Individual mother1 = new Individual("Mother1");
			mother1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1960"));
			mother1.setGender(Gender.F);

			Family family1 = new Family("Family1");
			family1.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2007"));
			family1.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));

			Individual[] individuals = { father1, mother1 };
			Family[] families = { family1 };

			assertTrue(MarriageBeforeDivorce.isValid(new GEDFile(individuals, families)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMarriageBeforeDivorceValidator2() {
		try {
			// test at max age difference
			Individual father1 = new Individual("Father2");
			father1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1940"));
			father1.setGender(Gender.M);

			Individual mother1 = new Individual("Mother2");
			mother1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1960"));
			mother1.setGender(Gender.F);

			Family family1 = new Family("Family2");
			family1.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2007"));
			family1.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));

			Individual[] individuals = { father1, mother1 };
			Family[] families = { family1 };

			assertTrue(MarriageBeforeDivorce.isValid(new GEDFile(individuals, families)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDivorceBeforeMarriageValidator() {
		try {
			// test at max age difference
			Individual father1 = new Individual("Father3");
			father1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1940"));
			father1.setGender(Gender.M);

			Individual mother1 = new Individual("Mother3");
			mother1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1960"));
			mother1.setGender(Gender.F);

			Family family1 = new Family("Family3");
			family1.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1995"));
			family1.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2006"));

			Individual[] individuals = { father1, mother1 };
			Family[] families = { family1 };

			assertFalse(MarriageBeforeDivorce.isValid(new GEDFile(individuals, families)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDivorceBeforeMarriageValidator2() {
		try {
			// test at max age difference
			Individual father1 = new Individual("Father4");
			father1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1940"));
			father1.setGender(Gender.M);

			Individual mother1 = new Individual("Mother4");
			mother1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1960"));
			mother1.setGender(Gender.F);

			Family family1 = new Family("Family4");
			family1.setDivorce(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"));
			family1.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2000"));

			Individual[] individuals = { father1, mother1 };
			Family[] families = { family1 };

			assertFalse(MarriageBeforeDivorce.isValid(new GEDFile(individuals, families)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNoDivorceDateValidator() {
		try {
			// test at max age difference
			Individual father1 = new Individual("Father5");
			father1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1940"));
			father1.setGender(Gender.M);

			Individual mother1 = new Individual("Mother5");
			mother1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1960"));
			mother1.setGender(Gender.F);

			Family family1 = new Family("Family5");
			family1.setMarriage(new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2000"));

			Individual[] individuals = { father1, mother1 };
			Family[] families = { family1 };

			assertTrue(MarriageBeforeDivorce.isValid(new GEDFile(individuals, families)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}