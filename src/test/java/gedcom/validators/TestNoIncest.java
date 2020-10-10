package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

public class TestNoIncest {

	Validator validator = new DefaultValidator();
	Validator noIncest = new NoIncest(validator);
	GEDFile ged;
	Family[] fams;
	Individual[] indivs;

	@BeforeEach
	public void setup() {
		Individual gpa = new Individual("gpa");
		Individual gma = new Individual("gma");
		Individual k1 = new Individual("sibling-1");
		Individual k2 = new Individual("sibling-2");
		Family fam = new Family("old_fam");
		fam.setHusband(gpa);
		fam.setWife(gma);
		fam.addChild(k1);
		fam.addChild(k2);
		gma.addSpouseFamily(fam);
		gpa.addSpouseFamily(fam);
		k1.addChildFamily(fam);
		k2.addChildFamily(fam);
		fams = new Family[] { fam };
		indivs = new Individual[] { gma, gpa, k1, k2 };
		this.ged = new GEDFile(indivs, fams);
	}

	@Test
	public void testNoIncest() {
		assertTrue(noIncest.isValid(ged));
	}

	@Test
	public void testSiblingIncest() {
		Family fam2 = new Family("fam2");
		fam2.setHusband(indivs[2]);
		fam2.setWife(indivs[3]);
		indivs[2].addSpouseFamily(fam2);
		indivs[2].addSpouseFamily(fam2);

		ged = new GEDFile(ged.getIndividuals().toArray(new Individual[ged.getIndividuals().size()]),
				new Family[] { ged.getFamilies().get(0), fam2 });
		assertFalse(noIncest.isValid(ged));
	}

	@Test
	public void testFirstCousinIncest() {

		Family subFamily1 = new Family("sub1"), subFamily2 = new Family("sub2");

		subFamily1.setHusband(indivs[2]);
		subFamily1.setWife(new Individual("k1-spouse"));
		Individual j1 = new Individual("cousin-1");
		subFamily1.addChild(j1);
		j1.addChildFamily(subFamily1);

		subFamily2.setWife(indivs[3]);
		subFamily2.setHusband(new Individual("cousin-2"));
		Individual j2 = new Individual("j2");
		subFamily2.addChild(j2);
		j2.addChildFamily(subFamily2);

		Family testFam = new Family("test");
		testFam.setHusband(j1);
		testFam.setWife(j2);
		j1.addSpouseFamily(testFam);
		j2.addSpouseFamily(testFam);

		ArrayList<Individual> newIndivs = new ArrayList<Individual>();
		for (Individual i : indivs)
			newIndivs.add(i);
		newIndivs.add(j1);
		newIndivs.add(j2);

		ged = new GEDFile(newIndivs.toArray(new Individual[newIndivs.size()]),
				new Family[] { ged.getFamilies().get(0), subFamily1, subFamily2, testFam });
		assertFalse(noIncest.isValid(ged));
	}

}
