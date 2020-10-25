package gedcom.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import gedcom.builders.*;

import org.junit.jupiter.api.Test;

public class TestFamily {

    private final int MAX_CHILDREN = 15;

    private Family family;

    @BeforeEach
    public void setup() {
        this.family = new FamilyBuilder().build();
    }

    @Test
    public void testFamilyID() {
        String FAMILY_ID = "ID";
        family = new Family(FAMILY_ID);
        assertEquals(FAMILY_ID, family.getID());
    }

    @Test
    public void testGetSetHusband() {
        Individual husband = new IndividualBuilder().male().build();
        family = new FamilyBuilder().withHusband(husband).build();
        assertEquals(husband, family.getHusband());
    }

    @Test
    public void testSetHusbandException1() {
        assertThrows(IllegalArgumentException.class, () -> {
            family.setHusband(null);
        });
    }

    @Test
    public void testSetHusbandException2() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            family.setHusband(new IndividualBuilder().female().build());
        });

        String expectedMessage = "Error US21:";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetSetWife() {
        Individual wife = new IndividualBuilder().female().build();
        family = new FamilyBuilder().withWife(wife).build();
        assertEquals(wife, family.getWife());
    }

    @Test
    public void testSetWifeException1() {
        assertThrows(IllegalArgumentException.class, () -> {
            family.setWife(null);
        });
    }

    @Test
    public void testSetWifeException2() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            family.setWife(new IndividualBuilder().male().build());
        });

        String expectedMessage = "Error US21:";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testMarriage() {
        Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date divorce = DateBuilder.build(1, Calendar.JANUARY, 2001);
        assertEquals(marriage, new FamilyBuilder().withMarriage(marriage).build().getMarriage());
        assertEquals(marriage, new FamilyBuilder().withDivorce(marriage).withMarriage(marriage).build().getMarriage());
        assertEquals(marriage, new FamilyBuilder().withDivorce(divorce).withMarriage(marriage).build().getMarriage());
    }

    @Test
    public void testMarriageException1() {
        assertThrows(IllegalArgumentException.class, () -> {
            Family family = new FamilyBuilder().build();
            family.setMarriage(null);
        });
    }

    @Test
    public void testMarriageException2() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2001);
            Date divorce = DateBuilder.build(1, Calendar.JANUARY, 2000);
            Family family = new FamilyBuilder().withDivorce(divorce).build();
            family.setMarriage(marriage);
        });

        String expectedMessage = "Error US04: Marriage cannot occur after divorce.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    public void testMarriageException3() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Date now = new Date();
            Date marriage = DateBuilder.newDateDaysAfter(now, 1);
            Family family = new FamilyBuilder().withMarriage(marriage).build();
        });

        String expectedMessage = "Error US01: Marriage must occur before the current time.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDivorce() {
        Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date divorce = DateBuilder.build(1, Calendar.JANUARY, 2001);
        assertEquals(divorce, new FamilyBuilder().withDivorce(divorce).build().getDivorce());
        assertEquals(divorce, new FamilyBuilder().withMarriage(divorce).withDivorce(divorce).build().getDivorce());
        assertEquals(divorce, new FamilyBuilder().withMarriage(marriage).withDivorce(divorce).build().getDivorce());
    }

    @Test
    public void testDivorceExcpetion1() {
        assertThrows(IllegalArgumentException.class, () -> {
            Family family = new FamilyBuilder().build();
            family.setDivorce(null);
        });
    }

    @Test
    public void testDivorceExcpetion2() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2001);
            Date divorce = DateBuilder.build(1, Calendar.JANUARY, 2000);
            Family family = new FamilyBuilder().withMarriage(marriage).build();
            family.setDivorce(divorce);
        });

        String expectedMessage = "Error US04: Divorce cannot occur before marriage.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void testDivorceExcpetion3() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Date now = new Date();
            Date marriage = DateBuilder.newDateDaysAfter(now, 1);
            Family family = new FamilyBuilder().withDivorce(marriage).build();
        });

        String expectedMessage = "Error US01: Divorce must occur before the current time.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddChildren() {
        for (int i = 0; i < MAX_CHILDREN; i++) {
            assertTrue(family.addChild(new IndividualBuilder().build()));
        }
    }

    @Test
    public void testAddChildrenExcpetion1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            family.addChild(null);
        });

        String expectedMessage = "Child cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddChildrenExcpetion2() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Individual child = new IndividualBuilder().build();
            family.addChild(child);
            family.addChild(child);
        });

        String expectedMessage = "Child already exists in family.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddChildrenExcpetion3() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            for (int i = 0; i < MAX_CHILDREN + 1; i++) {
                Individual child = new IndividualBuilder().build();
                assertTrue(family.addChild(child));
            }
        });

        String expectedMessage = "Error US22:";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testGetChildren() {
        Individual child1 = new IndividualBuilder().withBirth(1, Calendar.JANUARY, 2000).build();
        Individual child2 = new IndividualBuilder().withBirth(1, Calendar.JANUARY, 2001).build();
        Individual child3 = new IndividualBuilder().withBirth(1, Calendar.JANUARY, 2002).build();
        Individual child4 = new IndividualBuilder().withBirth(1, Calendar.JANUARY, 1999).build();
        Individual child5 = new IndividualBuilder().withBirth(1, Calendar.JANUARY, 1998).build();

        Family family = new FamilyBuilder().withChildren(child1, child2, child3, child4, child5).build();
        List<Individual> children = family.getChildren();

        Individual previousChild = children.get(0);
        Individual currentChild;
        for (int i = 1; i < children.size(); i++) {
            currentChild = children.get(i);
            assertTrue(previousChild.getBirthday().after(currentChild.getBirthday()));
            previousChild = currentChild;
        }
    }
}
