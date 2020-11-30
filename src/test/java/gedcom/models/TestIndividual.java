package gedcom.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gedcom.builders.*;
import gedcom.logging.Level;
import gedcom.logging.LogAppender;
import gedcom.logging.Logger;

public class TestIndividual {

    private final int N = 10;

    private LogAppender logAppender;

    @BeforeEach
    public void setup() {
        Logger logger = Logger.getInstance();
        logAppender = new LogAppender();
        logger.addAppender(logAppender);
        logAppender.start();
    }

    @AfterEach
    public void tearDown() {
        logAppender.stop();
    }

    @Test
    public void testID() {
        String ID = "ID";
        Individual individual = new Individual(ID);
        assertEquals(ID, individual.getID());
    }

    @Test
    public void testName() {
        String FIRST_NAME = "John";
        String LAST_NAME = "Smith";
        String FULL_NAME = String.format("%s /%s/", FIRST_NAME, LAST_NAME);

        Individual individual = Individual.builder().name(FULL_NAME).build();
        assertEquals(FULL_NAME, individual.getName());
        assertEquals(FIRST_NAME, individual.getFirstName());
        assertEquals(LAST_NAME, individual.getLastName());
    }

    @Test
    public void testBirth() {
        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date death = DateBuilder.build(1, Calendar.JANUARY, 2001);
        assertEquals(birth, Individual.builder().birth(birth).build().getBirthday());
        assertEquals(birth, Individual.builder().death(birth).birth(birth).build().getBirthday());
        assertEquals(birth, Individual.builder().death(death).birth(birth).build().getBirthday());
    }

    @Test
    public void testBirthException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Individual individual = Individual.builder().build();
            individual.setBirthday(null);
        });
    }

    @Test
    public void testBirthLogMessage1() {
        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2001);
        Date death = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Individual individual = Individual.builder().death(death).build();
        individual.setBirthday(birth);

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US03", Level.ERROR, 3));
    }

    @Test
    public void testBirthLogMessage2() {
        Date now = new Date();
        Date birth = DateBuilder.newDateDaysAfter(now, 1);
        Individual individual = Individual.builder().birth(birth).build();

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US01", Level.ERROR, 1));
    }

    @Test
    public void testDeath() {
        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date death = DateBuilder.build(1, Calendar.JANUARY, 2001);

        assertEquals(death, Individual.builder().death(death).build().getDeath());
        assertEquals(death, Individual.builder().birth(death).death(death).build().getDeath());
        assertEquals(death, Individual.builder().birth(birth).death(death).build().getDeath());
    }

    @Test
    public void testDeathException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Individual individual = Individual.builder().build();
            individual.setDeath(null);
        });
    }

    @Test
    public void testDeathLogMessage1() {
        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2001);
        Date death = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Individual individual = Individual.builder().birth(birth).build();
        individual.setDeath(death);

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US03", Level.ERROR, 3));
    }
    
    @Test
    public void testDeathLogMessage2() {
        Date now = new Date();
        Date death = DateBuilder.newDateDaysAfter(now, 1);
        Individual individual = Individual.builder().death(death).build();

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US01", Level.ERROR, 1));
    }

    @Test
    public void testAge() {
        int BIRTH_YEAR = 2000;
        int AGE = 10;

        Date birth = DateBuilder.build(1, Calendar.JANUARY, BIRTH_YEAR);
        Date death = DateBuilder.build(1, Calendar.JANUARY, BIRTH_YEAR + AGE);

        Individual individual = Individual.builder().birth(birth).death(death).build();

        assertEquals(AGE, individual.age());
    }

    @Test
    public void testAgeException() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Individual individual = Individual.builder().build();
            individual.age();
        });

        String expectedMessage = "Cannot determine age without a birth date.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testMaxAgeLogMessage() {
        Date birth = DateBuilder.build(1, Calendar.JANUARY, 1800);
        Date death = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Individual individual = Individual.builder().birth(birth).death(death).build();

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US07", Level.ANOMALY, 7));
    }

    @Test
    public void testAlive() {
        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date death = DateBuilder.build(1, Calendar.JANUARY, 2001);

        Individual individual = Individual.builder().birth(birth).build();
        assertTrue(individual.alive());

        individual.setDeath(death);
        assertFalse(individual.alive());
    }

    @Test
    public void testAliveException() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Individual individual = Individual.builder().build();
            individual.alive();
        });

        String expectedMessage = "Cannot determine living status without a birth date.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testIsSibling() {
        Individual child1 = Individual.builder().build();
        Individual child2 = Individual.builder().build();
        Individual child3 = Individual.builder().build();

        Family family1 = Family.builder().child(child1).child(child2).build();
        Family family2 = Family.builder().child(child2).child(child3).build();

        // Family 1
        assertTrue(child1.isSibling(child2));
        assertTrue(child2.isSibling(child1));

        // Family 2
        assertTrue(child2.isSibling(child3));
        assertTrue(child3.isSibling(child2));

        // Not siblings
        assertFalse(child1.isSibling(child3));
        assertFalse(child3.isSibling(child1));
    }
    
    @Test
    public void testGetSiblings() {
        Individual child1 = Individual.builder().build();
        Individual child2 = Individual.builder().build();

        Family family = Family.builder().child(child1).child(child2).build();

        assertTrue(orderAgnosticEquality(Arrays.asList(child1), child2.getSiblings()));
        assertTrue(orderAgnosticEquality(Arrays.asList(child2), child1.getSiblings()));
    }

    private <T> boolean orderAgnosticEquality(List<T> l1, List<T> l2) {
        boolean sameSize = l1.size() == l2.size();
        boolean sameContents = l1.containsAll(l2) && l2.containsAll(l1);
        return sameSize && sameContents;
    }

    @Test
    public void testGetChildren() {
        Individual parent = Individual.builder().build();

        Individual child1 = Individual.builder().birth(1, Calendar.JANUARY, 2000).build();
        Individual child2 = Individual.builder().birth(1, Calendar.JANUARY, 2001).build();
        Individual child3 = Individual.builder().birth(1, Calendar.JANUARY, 2002).build();
        Individual child4 = Individual.builder().birth(1, Calendar.JANUARY, 1999).build();
        Individual child5 = Individual.builder().birth(1, Calendar.JANUARY, 1998).build();

        Family family = Family.builder().husband(parent).children(child1, child2, child3, child4, child5).build();
        List<Individual> children = parent.getChildren();

        Individual previousChild = children.get(0);
        Individual currentChild;
        for (int i = 1; i < children.size(); i++) {
            currentChild = children.get(i);
            assertTrue(previousChild.getBirthday().after(currentChild.getBirthday()));
            previousChild = currentChild;
        }
    }

    @Test
    public void testGetSpouses() {
        Individual husband = Individual.builder().male().build();
        Individual firstWife = Individual.builder().female().build();
        Individual secondWife = Individual.builder().female().build();

        Individual firstChild = Individual.builder().build();
        Individual secondChild = Individual.builder().build();

        Family firstMarriage = Family.builder().husband(husband).wife(firstWife).child(firstChild).build();
        Family secondMarriage = Family.builder().husband(husband).wife(secondWife).child(secondChild).build();

        List<Individual> spouses = new ArrayList<>(Arrays.asList(firstWife, secondWife));
        List<Individual> indivSpouses = husband.getSpouses();

        assertTrue(orderAgnosticEquality(spouses, indivSpouses));
    }

    @Test
    public void testGetParents() {
        Individual father = new IndividualBuilder().male().build();
        Individual mother = new IndividualBuilder().female().build();
        Individual child = new IndividualBuilder().build();
        
        Family family = new FamilyBuilder().withHusband(father).withWife(mother).withChild(child).build();
        Individual father = Individual.builder().male().build();
        Individual mother = Individual.builder().female().build();
        Individual child = Individual.builder().build();

        Family family = Family.builder().husband(father).wife(mother).child(child).build();

        assertTrue(orderAgnosticEquality(Arrays.asList(father, mother),child.getParents()));
    }

    private List<Individual> createFamilyDescendants(Individual individual, int numDescendants) {
        List<Individual> descendants = new ArrayList<>();
        for (int i = 0; i < numDescendants; i++) {
            Individual child = Individual.builder().build();
            Family family = Family.builder().husband(individual).child(child).build();
            descendants.add(child);
            individual = child;
        }
        return descendants;
    }

    @Test
    public void testGetDescendants() {
        Individual individual = Individual.builder().build();

        List<Individual> descendants = createFamilyDescendants(individual, N);
        List<Individual> indivDescendants = individual.getDescendants();

        assertTrue(orderAgnosticEquality(descendants, indivDescendants));
    }

    @Test
    public void testGetDescendantsAt() {
        Individual individual = Individual.builder().build();
        List<Individual> descendants = createFamilyDescendants(individual, N);

        for (int i = 0; i < N - 1; i++) {
            List<Individual> descendantsAt = individual.getDescendantsAt(i + 1);
            assertEquals(1, descendantsAt.size());
            assertEquals(descendants.get(i), individual.getDescendantsAt(i + 1).get(0));
        }
    }

    @Test
    public void testIsDescendant() {
        Individual individual = Individual.builder().build();
        List<Individual> descendants = createFamilyDescendants(individual, N);

        for (Individual descendant : descendants) {
            assertTrue(descendant.isDescendant(individual));
        }
    }

    @Test
    public void testHasDescendant() {
        Individual individual = Individual.builder().build();
        List<Individual> descendants = createFamilyDescendants(individual, N);

        for (Individual descendant : descendants) {
            assertTrue(individual.hasDescendant(descendant));
        }
    }

    private List<Individual> createFamilyAncestors(Individual individual, int numAncestors) {
        List<Individual> ancestors = new ArrayList<>();
        for (int i = 0; i < numAncestors; i++) {
            Individual parent = Individual.builder().build();
            Family family = Family.builder().husband(parent).child(individual).build();
            ancestors.add(parent);
            individual = parent;
        }
        return ancestors;
    }

    @Test
    public void testGetAncestors() {
        Individual individual = Individual.builder().build();

        List<Individual> ancestors = createFamilyAncestors(individual, N);
        List<Individual> indivAncestors = individual.getAncestors();

        assertTrue(orderAgnosticEquality(ancestors, indivAncestors));
    }

    @Test
    public void testGetAncestorsAt() {
        Individual individual = Individual.builder().build();
        List<Individual> ancestors = createFamilyAncestors(individual, N);

        for (int i = 0; i < N - 1; i++) {
            List<Individual> ancestorsAt = individual.getAncestorsAt(i + 1);
            assertEquals(1, ancestorsAt.size());
            assertEquals(ancestors.get(i), ancestorsAt.get(0));
        }
    }

    @Test
    public void testIsAncestor() {
        Individual individual = Individual.builder().build();
        List<Individual> ancestors = createFamilyAncestors(individual, N);

        for (Individual ancestor : ancestors) {
            assertTrue(ancestor.isAncestor(individual));
        }
    }

    @Test
    public void testHasAncestor() {
        Individual individual = Individual.builder().build();
        List<Individual> ancestors = createFamilyAncestors(individual, N);

        for (Individual ancestor : ancestors) {
            assertTrue(individual.hasAncestor(ancestor));
        }
    }

    private List<Individual> createFamilyCousins(Individual individual, int numCousins) {
        List<Individual> cousins = new ArrayList<>();
        List<Individual> ancestors = createFamilyAncestors(individual, numCousins + 1);
        for (int i = 1; i <= numCousins; i++) {
            Individual commonAncestor = ancestors.get(i);
            List<Individual> descendants = createFamilyDescendants(commonAncestor, i + 1);
            cousins.add(descendants.get(i));
        }
        return cousins;
    }

    @Test
    void testGetCousins() {
        Individual individual = Individual.builder().build();
        List<Individual> cousins = createFamilyCousins(individual, N);

        for (int i = 1; i <= N; i++) {
            List<Individual> indivCousins = individual.getCousins(i);
            assertEquals(1, indivCousins.size());
            assertEquals(cousins.get(i - 1), indivCousins.get(0));
        }
    }
    
	@Test
	public void testGetGeneration() {
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

		assertTrue(individual.getGeneration() == 4);
	}

}
