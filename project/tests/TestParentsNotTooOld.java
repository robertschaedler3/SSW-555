package project.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import project.Validator;
import project.interfaces.Gender;
import project.models.*;
import project.validators.DefaultValidator;
import project.validators.ParentsNotTooOld;

import org.junit.jupiter.api.Test;

class TestParentsNotTooOld {

	Validator validator = new DefaultValidator();
	Validator parentsNotTooOldValidator = new ParentsNotTooOld(validator);

	@Test
	void testParentsNotTooOldValidator() {
		try {
			Individual child1 = new Individual("Child1");
			child1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
			child1.addChildFamily("Family1");

			Individual father1 = new Individual("Father1");
			father1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1940"));
			father1.setGender(Gender.M);
			father1.addSpouseFamily("Family1");

			Individual mother1 = new Individual("Mother1");
			mother1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1960"));
			mother1.setGender(Gender.F);
			mother1.addSpouseFamily("Family1");
			
			Individual[] individuals = {child1, father1, mother1};
			Family[] families = {new Family("Family1")};
			
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
			child2.addChildFamily("Family1");
			child2.addSpouseFamily("Family2");
			child2.setGender(Gender.M);
			
			Individual grandchild = new Individual("grandchild1");
			grandchild.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2080"));
			grandchild.addChildFamily("Family2");
			
			Individual[] individuals2 = {child1, father1, mother1, child2, grandchild};
			Family[] families2 = {new Family("Family1"), new Family("Family2")};
			// test with two families - a grandchild added
			assertTrue(parentsNotTooOldValidator.isValid(new GEDFile(individuals2, families2)));
			
			// test for child-father too old for grandchild
			grandchild.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2110"));			
			assertFalse(parentsNotTooOldValidator.isValid(new GEDFile(individuals2, families2)));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
