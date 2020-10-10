package gedcom.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import gedcom.interfaces.Gender;
import gedcom.models.*;

import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

public class TestParentsNotTooOld {

	Validator validator = new DefaultValidator();
	Validator parentsNotTooOldValidator = new ParentsNotTooOld(validator);

	@Test
	public void testParentsNotTooOldValidator() {
		try {

			Family family1 = new Family("Family1");
			Family family2 = new Family("Family2");

			Individual child1 = new Individual("Child1");
			child1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
			child1.addChildFamily(family1);

			Individual father1 = new Individual("Father1");
			father1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1940"));
			father1.setGender(Gender.M);
			father1.addSpouseFamily(family1);

			Individual mother1 = new Individual("Mother1");
			mother1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1960"));
			mother1.setGender(Gender.F);
			mother1.addSpouseFamily(family1);

			Individual[] individuals = { child1, father1, mother1 };
			family1.setHusband(father1);
			family1.setWife(mother1);
			family1.addChild(child1);
			Family[] families = { family1 };

			// test at max age difference
			assertTrue(parentsNotTooOldValidator.isValid(new GEDFile(individuals, families)));

			// test mother too old
			mother1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1959"));
			assertFalse(parentsNotTooOldValidator.isValid(new GEDFile(individuals, families)));

			// test father too old (and reset motherage)
			mother1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1960"));
			father1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1939"));
			assertFalse(parentsNotTooOldValidator.isValid(new GEDFile(individuals, families)));

			mother1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1980"));
			father1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1980"));

			Individual child2 = new Individual("Child2");
			child2.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
			child2.addChildFamily(family1);
			child2.addSpouseFamily(family2);
			child2.setGender(Gender.M);

			Individual grandchild = new Individual("grandchild1");
			grandchild.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2080"));
			grandchild.addChildFamily(family2);

			Individual[] individuals2 = { child1, father1, mother1, child2, grandchild };
			family1.addChild(child1);
			family1.setHusband(father1);
			family1.setWife(mother1);
			family1.addChild(child2);
			family2.setHusband(child2);
			family2.addChild(grandchild);
			Family[] families2 = { family1, family2 };
			// test with two families - a grandchild added
			assertTrue(parentsNotTooOldValidator.isValid(new GEDFile(individuals2, families2)));

			// test for child-father too old for grandchild
			grandchild.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2110"));
			assertFalse(parentsNotTooOldValidator.isValid(new GEDFile(individuals2, families2)));

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
