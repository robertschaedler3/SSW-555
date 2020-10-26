package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import gedcom.builders.FamilyBuilder;
import gedcom.builders.GEDFileBuilder;
import gedcom.builders.IndividualBuilder;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestCorrespondingEntries {

	Validator validator = new CorrespondingEntries(new DefaultValidator());

	@Test
	public void testCorrespondingEntries() {
		Individual husband = new IndividualBuilder().build();
		Individual wife = new IndividualBuilder().build();

		Family family1 = new FamilyBuilder().withHusband(husband).withWife(wife).withChildren(2).build();

		List<Individual> individuals = new ArrayList<>(Arrays.asList(husband, wife));
		List<Family> families = new ArrayList<>(Arrays.asList(family1));
		individuals.addAll(family1.getChildren());

		GEDFile gedFile = new GEDFile(individuals, families);
		assertTrue(validator.isValid(gedFile));
	}

	@Test
	public void testNonCorrespondingEntries() {
		Individual husband = new IndividualBuilder().build();
		Individual wife = new IndividualBuilder().build();

		Family family1 = new FamilyBuilder().withHusband(husband).withWife(wife).withChildren(2).build();

		List<Individual> individuals = new ArrayList<>();
		List<Family> families = new ArrayList<>(Arrays.asList(family1));
		individuals.addAll(family1.getChildren());

		GEDFile gedFile = new GEDFile(individuals, families);
		assertFalse(validator.isValid(gedFile));
	}

}
