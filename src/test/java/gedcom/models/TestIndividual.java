package gedcom.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import gedcom.builders.*;

public class TestIndividual {

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

        Individual individual = new IndividualBuilder().withName(FULL_NAME).build();
        assertEquals(FULL_NAME, individual.getName());
        assertEquals(FIRST_NAME, individual.getFirstName());
        assertEquals(LAST_NAME, individual.getLastName());
    }

    @Test
    public void testBirth() {
        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date death = DateBuilder.build(1, Calendar.JANUARY, 2001);
        assertEquals(birth, new IndividualBuilder().withBirth(birth).build().getBirthday());
        assertEquals(birth, new IndividualBuilder().withDeath(birth).withBirth(birth).build().getBirthday());
        assertEquals(birth, new IndividualBuilder().withDeath(death).withBirth(birth).build().getBirthday());
    }

    @Test
    public void testBirthException1() {
        assertThrows(IllegalArgumentException.class, () -> {
            Individual individual = new IndividualBuilder().build();
            individual.setBirthday(null);
        });
    }

    @Test
    public void testBirthException2() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Date birth = DateBuilder.build(1, Calendar.JANUARY, 2001);
            Date death = DateBuilder.build(1, Calendar.JANUARY, 2000);
            Individual individual = new IndividualBuilder().withDeath(death).build();
            individual.setBirthday(birth);
        });

        String expectedMessage = "US03: Birth cannot occur after death.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDeath() {
        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date death = DateBuilder.build(1, Calendar.JANUARY, 2001);
        assertEquals(death, new IndividualBuilder().withDeath(death).build().getDeath());
        assertEquals(death, new IndividualBuilder().withBirth(death).withDeath(death).build().getDeath());
        assertEquals(death, new IndividualBuilder().withBirth(birth).withDeath(death).build().getDeath());
    }

    @Test
    public void testDeathException1() {
        assertThrows(IllegalArgumentException.class, () -> {
            Individual individual = new IndividualBuilder().build();
            individual.setDeath(null);
        });
    }

    @Test
    public void testDeathException2() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Date birth = DateBuilder.build(1, Calendar.JANUARY, 2001);
            Date death = DateBuilder.build(1, Calendar.JANUARY, 2000);
            Individual individual = new IndividualBuilder().withBirth(birth).build();
            individual.setDeath(death);
        });

        String expectedMessage = "US03: Death cannot occur before birth.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAge() {
        int BIRTH_YEAR = 2000;
        int AGE = 10;

        Date birth = DateBuilder.build(1, Calendar.JANUARY, BIRTH_YEAR);
        Date death = DateBuilder.build(1, Calendar.JANUARY, BIRTH_YEAR + AGE);

        Individual individual = new IndividualBuilder().withBirth(birth).withDeath(death).build();

        assertEquals(AGE, individual.age());
    }

    @Test
    public void testAgeException() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Individual individual = new IndividualBuilder().build();
            individual.age();
        });

        String expectedMessage = "Cannot determine age without a birth date.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAlive() {
        Date birth = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date death = DateBuilder.build(1, Calendar.JANUARY, 2001);

        Individual individual = new IndividualBuilder().withBirth(birth).build();
        assertTrue(individual.alive());

        individual.setDeath(death);
        assertFalse(individual.alive());
    }

    @Test
    public void testAliveException() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Individual individual = new IndividualBuilder().build();
            individual.alive();
        });

        String expectedMessage = "Cannot determine living status without a birth date.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private <T> boolean orderAgnosticEquality(List<T> l1, List<T> l2) {
        boolean sameSize = l1.size() == l2.size();
        boolean sameContents = l1.containsAll(l2) && l2.containsAll(l1);
        return sameSize && sameContents;
    }

    @Test
    public void testGetChildren() {
        Individual husband = new IndividualBuilder().male().build();
        Individual firstWife = new IndividualBuilder().female().build();
        Individual secondWife = new IndividualBuilder().female().build();

        Individual firstChild = new IndividualBuilder().build();
        Individual secondChild = new IndividualBuilder().build();

        Family firstMarriage = new FamilyBuilder().withHusband(husband).withWife(firstWife).withChild(firstChild).build();
        Family secondMarriage = new FamilyBuilder().withHusband(husband).withWife(secondWife).withChild(secondChild).build();

        List<Individual> children = new ArrayList<>(Arrays.asList(firstChild, secondChild));
        List<Individual> indivChildren = husband.getChildren();

        assertTrue(orderAgnosticEquality(children, indivChildren));
    }

    @Test
    public void testGetSpouses() {
        Individual husband = new IndividualBuilder().male().build();
        Individual firstWife = new IndividualBuilder().female().build();
        Individual secondWife = new IndividualBuilder().female().build();

        Individual firstChild = new IndividualBuilder().build();
        Individual secondChild = new IndividualBuilder().build();

        Family firstMarriage = new FamilyBuilder().withHusband(husband).withWife(firstWife).withChild(firstChild).build();
        Family secondMarriage = new FamilyBuilder().withHusband(husband).withWife(secondWife).withChild(secondChild).build();

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

        assertTrue(orderAgnosticEquality(Arrays.asList(father, mother), child.getAncestorsAt(1)));
        // assertTrue(orderAgnosticEquality(Arrays.asList(father, mother), child.getParents()));
    }
    
    private List<Individual> createFamilyDescendants(Individual individual, int numDescendants) {
        List<Individual> descendants = new ArrayList<>();

        for (int i = 0; i < numDescendants; i++) {
            Individual child = new IndividualBuilder().build();
            Family family = new FamilyBuilder().withHusband(individual).withChild(child).build();
            descendants.add(child);
            individual = child;
        }

        return descendants;
    }

    @Test
    public void testGetDescendants() {
        int N = 10;
        Individual individual = new IndividualBuilder().build();

        List<Individual> descendants = createFamilyDescendants(individual, N);
        List<Individual> indivDescendants = individual.getDescendants();

        assertTrue(orderAgnosticEquality(descendants, indivDescendants));
    }
    
    @Test
    public void testGetDescendantsAt() {
        int N = 10;
        Individual individual = new IndividualBuilder().build();
        List<Individual> descendants = createFamilyDescendants(individual, N);

        for (int i = 0; i < N - 1; i++) {
            assertEquals(descendants.get(i), individual.getDescendantsAt(i + 1).get(0));
        }
    }
    
    @Test
    public void testIsDescendant() {
        int N = 10;
        Individual individual = new IndividualBuilder().build();
        List<Individual> descendants = createFamilyDescendants(individual, N);

        for (Individual descendant : descendants) {
            assertTrue(descendant.isDescendant(individual));
        }
    }

    @Test
    public void testHasDescendant() {
        int N = 10;
        Individual individual = new IndividualBuilder().build();
        List<Individual> descendants = createFamilyDescendants(individual, N);

        for (Individual descendant : descendants) {
            assertTrue(individual.hasDescendant(descendant));
        }
    }

    private List<Individual> createFamilyAncestors(Individual individual, int numAncestors) {
        List<Individual> ancestors = new ArrayList<>();

        for (int i = 0; i < numAncestors; i++) {
            Individual parent = new IndividualBuilder().build();
            Family family = new FamilyBuilder().withHusband(parent).withChild(individual).build();
            ancestors.add(parent);
            individual = parent;
        }

        return ancestors;
    }

    @Test
    public void testGetAncestors() {
        int N = 10;
        Individual individual = new IndividualBuilder().build();

        List<Individual> ancestors = createFamilyAncestors(individual, N);
        List<Individual> indivAncestors = individual.getAncestors();

        assertTrue(orderAgnosticEquality(ancestors, indivAncestors));
    }
    
    @Test
    public void testGetAncestorsAt() {
        int N = 10;
        Individual individual = new IndividualBuilder().build();
        List<Individual> ancestors = createFamilyAncestors(individual, N);

        
        for (int i = 0; i < N - 1; i++) {
            individual.getAncestors();
            assertEquals(ancestors.get(i), individual.getAncestorsAt(i + 1).get(0));
        }
    }

    @Test
    public void testIsAncestor() {
        int N = 10;
        Individual individual = new IndividualBuilder().build();
        List<Individual> ancestors = createFamilyAncestors(individual, N);

        for (Individual ancestor : ancestors) {
            assertTrue(ancestor.isAncestor(individual));
        }
    }

    @Test
    public void testHasAncestor() {
        int N = 10;
        Individual individual = new IndividualBuilder().build();
        List<Individual> ancestors = createFamilyAncestors(individual, N);

        for (Individual ancestor : ancestors) {
            assertTrue(individual.hasAncestor(ancestor));
        }
    }

}
