package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import gedcom.builders.FamilyBuilder;
import gedcom.builders.GEDFileBuilder;
import gedcom.builders.IndividualBuilder;
import gedcom.models.*;

import org.junit.jupiter.api.Test;

public class TestParentsNotTooOld {

	Validator validator = new ParentsNotTooOld(new DefaultValidator());

	@Test
	public void testValid() {
		int childBirthYear = 2000;
		int fatherBirthYear = (childBirthYear - Family.FATHER_AGE_THRESHOLD) + 1;
		int motherBirthYear = (childBirthYear - Family.MOTHER_AGE_THRESHOLD) + 1;

		Individual father = new IndividualBuilder().withBirth(1, Calendar.JANUARY, fatherBirthYear).build();
		Individual mother = new IndividualBuilder().withBirth(1, Calendar.JANUARY, motherBirthYear).build();
		Individual child = new IndividualBuilder().withBirth(1, Calendar.JANUARY, childBirthYear).build();

		Family family = new FamilyBuilder().withSpouses(father, mother).withChild(child).build();

		GEDFile gedFile = new GEDFileBuilder().withIndividuals(father, mother, child).withFamily(family).build();

		assertTrue(validator.isValid(gedFile));
	}

	@Test
	public void testInvalidMother() {
		int childBirthYear = 2000;
		int fatherBirthYear = (childBirthYear - Family.FATHER_AGE_THRESHOLD) + 1;
		int motherBirthYear = childBirthYear - (Family.MOTHER_AGE_THRESHOLD + 1);

		Individual father = new IndividualBuilder().withBirth(1, Calendar.JANUARY, fatherBirthYear).build();
		Individual mother = new IndividualBuilder().withBirth(1, Calendar.JANUARY, motherBirthYear).build();
		Individual child = new IndividualBuilder().withBirth(1, Calendar.JANUARY, childBirthYear).build();

		Family family = new FamilyBuilder().withSpouses(father, mother).withChild(child).build();

		GEDFile gedFile = new GEDFileBuilder().withIndividuals(father, mother, child).withFamily(family).build();

		assertFalse(validator.isValid(gedFile));
	}

	@Test
	public void testInvalidFather() {
		int childBirthYear = 2000;
		int fatherBirthYear = childBirthYear - (Family.FATHER_AGE_THRESHOLD + 1);
		int motherBirthYear = (childBirthYear - Family.MOTHER_AGE_THRESHOLD) + 1;

		Individual father = new IndividualBuilder().withBirth(1, Calendar.JANUARY, fatherBirthYear).build();
		Individual mother = new IndividualBuilder().withBirth(1, Calendar.JANUARY, motherBirthYear).build();
		Individual child = new IndividualBuilder().withBirth(1, Calendar.JANUARY, childBirthYear).build();

		Family family = new FamilyBuilder().withSpouses(father, mother).withChild(child).build();

		GEDFile gedFile = new GEDFileBuilder().withIndividuals(father, mother, child).withFamily(family).build();

		assertFalse(validator.isValid(gedFile));
	}

}
