package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

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
		Individual husband = Individual.builder().build();
		Individual wife = Individual.builder().build();

		Family family1 = Family.builder().husband(husband).wife(wife).children(2).build();

		List<Individual> individuals = new ArrayList<>(Arrays.asList(husband, wife));
		List<Family> families = new ArrayList<>(Arrays.asList(family1));
		individuals.addAll(family1.getChildren());

		GEDFile gedFile = new GEDFile(individuals, families);
		assertTrue(validator.isValid(gedFile));
	}

	@Test
	public void testNonCorrespondingEntries() {
		Individual husband = Individual.builder().build();
		Individual wife = Individual.builder().build();

		Family family1 = Family.builder().husband(husband).wife(wife).children(2).build();

		List<Individual> individuals = new ArrayList<>();
		List<Family> families = new ArrayList<>(Arrays.asList(family1));
		individuals.addAll(family1.getChildren());

		GEDFile gedFile = new GEDFile(individuals, families);
		assertFalse(validator.isValid(gedFile));
	}

}
