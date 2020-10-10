package gedcom.models;

import static org.junit.Assert.*;

import org.junit.Test;

import gedcom.interfaces.Gender;

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

    @Test(expected = IllegalArgumentException.class)
    public void testSetHusbandException() {
        family.setHusband(null);
    }

    @Test
    public void testGetSetWife() {
        Individual wife = new Individual("WIFE");
        wife.setGender(Gender.F);
        family.setWife(wife);
        assertEquals(wife, family.getWife());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWifeException() {
        family.setWife(null);
    }

    @Test
    public void testAddChildren() {
        resetFamily();
        for (int i = 0; i < MAX_CHILDREN; i++) {
            Individual child = new Individual(String.format("CHILD%d", i));
            assertTrue(family.addChild(child));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenExcpetion1() {
        resetFamily();
        family.addChild(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChildrenExcpetion2() {
        resetFamily();
        Individual child = new Individual("CHILD");
        family.addChild(child);
        family.addChild(child);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddChildrenExcpetion3() {
        resetFamily();
        for (int i = 0; i < MAX_CHILDREN + 1; i++) {
            Individual child = new Individual(String.format("CHILD%d", i));
            assertTrue(family.addChild(child));
        }
    }
}
