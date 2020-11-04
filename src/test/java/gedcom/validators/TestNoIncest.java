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

public class TestNoIncest {

	Validator validator = new NoIncest(new DefaultValidator());

	@Test
	public void testNoIncest() {
		Individual uncle = new IndividualBuilder().build();
		Individual husband = new IndividualBuilder().build();
		Individual wife = new IndividualBuilder().build();

		Family family1 = new FamilyBuilder().withHusband(husband).withWife(wife).withChildren(2).build();
		Family family2 = new FamilyBuilder().withChildren(husband, uncle).build();

		List<Individual> individuals = new ArrayList<>(Arrays.asList(uncle, husband, wife));
		List<Family> families = new ArrayList<>(Arrays.asList(family1, family2));
		individuals.addAll(family1.getChildren());

		GEDFile gedFile = new GEDFile(individuals, families);
		assertTrue(validator.isValid(gedFile));
	}

	@Test
	public void testSiblingIncest() {
		Individual husband = new IndividualBuilder().build();
		Individual wife = new IndividualBuilder().build();

		Family family1 = new FamilyBuilder().withChildren(husband, wife).build();
		Family family2 = new FamilyBuilder().withHusband(husband).withWife(wife).build();

		GEDFile gedFile = new GEDFileBuilder().withIndividuals(husband, wife).withFamilies(family1, family2).build();
		assertFalse(validator.isValid(gedFile));
	}

	@Test
	public void testFirstCousinIncest() {
		Individual individual = new IndividualBuilder().build();
		
		Individual grandparent = new IndividualBuilder().build();
		Individual parent = new IndividualBuilder().build();

		Individual uncle = new IndividualBuilder().build();
		Individual cousin = new IndividualBuilder().build();

		Family family1 = new FamilyBuilder().withHusband(grandparent).withChildren(parent, uncle).build();
		Family family2 = new FamilyBuilder().withHusband(parent).withChild(individual).build();
		Family family3 = new FamilyBuilder().withHusband(uncle).withChild(cousin).build();

		Family family4 = new FamilyBuilder().withHusband(cousin).withWife(individual).build();

		List<Individual> individuals = Arrays.asList(grandparent, parent, individual, uncle, cousin);
		List<Family> families = Arrays.asList(family1, family2, family3, family4);

		GEDFile gedFile = new GEDFileBuilder().withIndividuals(individuals).withFamilies(families).build();
		assertFalse(validator.isValid(gedFile));
	}

	@Test
	public void testAuntUncleIncest() {
		Individual individual = new IndividualBuilder().build();

		Individual parent = new IndividualBuilder().build();
		Individual uncle = new IndividualBuilder().build();

		Family family1 = new FamilyBuilder().withChildren(parent, uncle).build();
		Family family2 = new FamilyBuilder().withHusband(parent).withChild(individual).build();

		Family family3 = new FamilyBuilder().withHusband(uncle).withWife(individual).build();

		List<Individual> individuals = Arrays.asList(parent, individual, uncle);
		List<Family> families = Arrays.asList(family1, family2, family3);

		GEDFile gedFile = new GEDFileBuilder().withIndividuals(individuals).withFamilies(families).build();
		assertFalse(validator.isValid(gedFile));
	}
	
	@Test
	public void testDescendantIncest() {

		IndividualBuilder individualBuilder = new IndividualBuilder();
		Individual individual = individualBuilder.build();

		Individual parent = individualBuilder.build();
		Individual grandparent = individualBuilder.build();
		Individual greatgrandparent = individualBuilder.build();

		FamilyBuilder familyBuilder = new FamilyBuilder();
		familyBuilder.withHusband(greatgrandparent).withChild(grandparent).build();
		familyBuilder.withHusband(grandparent).withChild(parent).build();
		familyBuilder.withHusband(parent).withChild(individual).build();
		familyBuilder.withHusband(greatgrandparent).withWife(individual).build();

		GEDFile gedFile = new GEDFile(individualBuilder.getIndividuals(), familyBuilder.getFamilies());
		assertFalse(validator.isValid(gedFile));
	}

}
