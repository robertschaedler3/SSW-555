package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import gedcom.models.*;

import org.junit.jupiter.api.Test;

public class TestParentsNotTooOld {

	Validator validator = new ParentsNotTooOld(new DefaultValidator());

	@Test
	public void testValid() {
		int childBirthYear = 2000;
		int fatherBirthYear = (childBirthYear - Family.FATHER_AGE_THRESHOLD) + 1;
		int motherBirthYear = (childBirthYear - Family.MOTHER_AGE_THRESHOLD) + 1;

		Individual father = Individual.builder().birth(1, Calendar.JANUARY, fatherBirthYear).build();
		Individual mother = Individual.builder().birth(1, Calendar.JANUARY, motherBirthYear).build();
		Individual child = Individual.builder().birth(1, Calendar.JANUARY, childBirthYear).build();

		Family family = Family.builder().spouses(father, mother).child(child).build();

		GEDFile gedFile = GEDFile.builder().individuals(father, mother, child).family(family).build();

		assertTrue(validator.isValid(gedFile));
	}

	@Test
	public void testInvalidMother() {
		int childBirthYear = 2000;
		int fatherBirthYear = (childBirthYear - Family.FATHER_AGE_THRESHOLD) + 1;
		int motherBirthYear = childBirthYear - (Family.MOTHER_AGE_THRESHOLD + 1);

		Individual father = Individual.builder().birth(1, Calendar.JANUARY, fatherBirthYear).build();
		Individual mother = Individual.builder().birth(1, Calendar.JANUARY, motherBirthYear).build();
		Individual child = Individual.builder().birth(1, Calendar.JANUARY, childBirthYear).build();

		Family family = Family.builder().spouses(father, mother).child(child).build();

		GEDFile gedFile = GEDFile.builder().individuals(father, mother, child).family(family).build();

		assertFalse(validator.isValid(gedFile));
	}

	@Test
	public void testInvalidFather() {
		int childBirthYear = 2000;
		int fatherBirthYear = childBirthYear - (Family.FATHER_AGE_THRESHOLD + 1);
		int motherBirthYear = (childBirthYear - Family.MOTHER_AGE_THRESHOLD) + 1;

		Individual father = Individual.builder().birth(1, Calendar.JANUARY, fatherBirthYear).build();
		Individual mother = Individual.builder().birth(1, Calendar.JANUARY, motherBirthYear).build();
		Individual child = Individual.builder().birth(1, Calendar.JANUARY, childBirthYear).build();

		Family family = Family.builder().spouses(father, mother).child(child).build();

		GEDFile gedFile = GEDFile.builder().individuals(father, mother, child).family(family).build();

		assertFalse(validator.isValid(gedFile));
	}

}
