package gedcom.models;

import gedcom.interfaces.Gender;

import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

public class TestFamily {

    private final String FAMILY_ID = "FAMILY";
    private final int MAX_CHILDREN = 15;

    private Family family;

    public TestFamily() {
        this.family = new Family(FAMILY_ID);
    }

    private void resetFamily() {
        family = new Family(FAMILY_ID);
    }

    @Test
    public void testFamilyID() {
        assertEquals(FAMILY_ID, family.getID());
    }

    @Test
    public void testGetSetHusband() {
        Individual husband = new Individual("HUSBAND");
        husband.setGender(Gender.M);
        family.setHusband(husband);
        assertEquals(husband, family.getHusband());
    }

    @Test
    public void testSetHusbandException() {
        assertThrows(IllegalArgumentException.class, () -> {
            family.setHusband(null);
        });
    }

    @Test
    public void testGetSetWife() {
        Individual wife = new Individual("WIFE");
        wife.setGender(Gender.F);
        family.setWife(wife);
        assertEquals(wife, family.getWife());
    }

    @Test
    public void testSetWifeException() {
        assertThrows(IllegalArgumentException.class, () -> {
            family.setWife(null);
        });
    }

    @Test
    public void testAddChildren() {
        resetFamily();
        for (int i = 0; i < MAX_CHILDREN; i++) {
            Individual child = new Individual(String.format("CHILD%d", i));
            assertTrue(family.addChild(child));
        }
    }

    @Test
    public void testAddChildrenExcpetion1() {
        resetFamily();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            family.addChild(null);
        });

        String expectedMessage = "Child cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddChildrenExcpetion2() {
        resetFamily();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Individual child = new Individual("CHILD");
            family.addChild(child);
            family.addChild(child);
        });

        String expectedMessage = "Child already exists in family.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddChildrenExcpetion3() {
        resetFamily();
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            for (int i = 0; i < MAX_CHILDREN + 1; i++) {
                Individual child = new Individual(String.format("CHILD%d", i));
                assertTrue(family.addChild(child));
            }
        });

        String expectedMessage = "Error US22:";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }
}
