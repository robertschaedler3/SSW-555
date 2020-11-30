package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestNoIncest {

	Validator validator = new NoIncest(new DefaultValidator());

	@Test
	public void testNoIncest() {
		Individual uncle = Individual.builder().build();
		Individual husband = Individual.builder().build();
		Individual wife = Individual.builder().build();

		Family family1 = Family.builder().husband(husband).wife(wife).children(2).build();
		Family family2 = Family.builder().children(husband, uncle).build();

		List<Individual> individuals = new ArrayList<>(Arrays.asList(uncle, husband, wife));
		List<Family> families = new ArrayList<>(Arrays.asList(family1, family2));
		individuals.addAll(family1.getChildren());

		GEDFile gedFile = new GEDFile(individuals, families);
		assertTrue(validator.isValid(gedFile));
	}

	@Test
	public void testSiblingIncest() {
		Individual husband = Individual.builder().build();
		Individual wife = Individual.builder().build();

		Family family1 = Family.builder().children(husband, wife).build();
		Family family2 = Family.builder().husband(husband).wife(wife).build();

		GEDFile gedFile = GEDFile.builder().individuals(husband, wife).families(family1, family2).build();
		assertFalse(validator.isValid(gedFile));
	}

	@Test
	public void testFirstCousinIncest() {
		Individual individual = Individual.builder().build();
		
		Individual grandparent = Individual.builder().build();
		Individual parent = Individual.builder().build();

		Individual uncle = Individual.builder().build();
		Individual cousin = Individual.builder().build();

		Family family1 = Family.builder().husband(grandparent).children(parent, uncle).build();
		Family family2 = Family.builder().husband(parent).child(individual).build();
		Family family3 = Family.builder().husband(uncle).child(cousin).build();

		Family family4 = Family.builder().husband(cousin).wife(individual).build();

		List<Individual> individuals = Arrays.asList(grandparent, parent, individual, uncle, cousin);
		List<Family> families = Arrays.asList(family1, family2, family3, family4);

		GEDFile gedFile = GEDFile.builder().individuals(individuals).families(families).build();
		assertFalse(validator.isValid(gedFile));
	}

	@Test
	public void testAuntUncleIncest() {
		Individual individual = Individual.builder().build();

		Individual parent = Individual.builder().build();
		Individual uncle = Individual.builder().build();

		Family family1 = Family.builder().children(parent, uncle).build();
		Family family2 = Family.builder().husband(parent).child(individual).build();

		Family family3 = Family.builder().husband(uncle).wife(individual).build();

		List<Individual> individuals = Arrays.asList(parent, individual, uncle);
		List<Family> families = Arrays.asList(family1, family2, family3);

		GEDFile gedFile = GEDFile.builder().individuals(individuals).families(families).build();
		assertFalse(validator.isValid(gedFile));
	}
	
	@Test
	public void testDescendantIncest() {

		Individual.Builder individualBuilder = Individual.builder();
		Individual individual = individualBuilder.build();

		Individual parent = individualBuilder.build();
		Individual grandparent = individualBuilder.build();
		Individual greatgrandparent = individualBuilder.build();

		Family.Builder familyBuilder = Family.builder();
		familyBuilder.husband(greatgrandparent).child(grandparent).build();
		familyBuilder.husband(grandparent).child(parent).build();
		familyBuilder.husband(parent).child(individual).build();
		familyBuilder.husband(greatgrandparent).wife(individual).build();

		GEDFile gedFile = new GEDFile(individualBuilder.getIndividuals(), familyBuilder.getFamilies());
		assertFalse(validator.isValid(gedFile));
	}

}
