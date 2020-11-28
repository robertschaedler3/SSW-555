package gedcom.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import gedcom.builders.*;
import gedcom.logging.Level;
import gedcom.logging.LogAppender;
import gedcom.logging.Logger;

import org.junit.jupiter.api.Test;

public class TestFamily {

    private Family family;

    private LogAppender logAppender;

    @BeforeEach
    public void setup() {
        family = Family.builder().build();

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
    public void testFamilyID() {
        String FAMILY_ID = "ID";
        family = new Family(FAMILY_ID);
        assertEquals(FAMILY_ID, family.getID());
    }

    @Test
    public void testGetSetHusband() {
        Individual husband = Individual.builder().male().build();
        family = Family.builder().husband(husband).build();
        assertEquals(husband, family.getHusband());
    }

    @Test
    public void testSetHusbandException() {
        assertThrows(IllegalArgumentException.class, () -> {
            family.setHusband(null);
        });
    }

    @Test
    public void testSetHusbandLogMessage() {
        family.setHusband(Individual.builder().female().build());

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US21", Level.ERROR, 21));
    }

    @Test
    public void testGetSetWife() {
        Individual wife = Individual.builder().female().build();
        family = Family.builder().wife(wife).build();
        assertEquals(wife, family.getWife());
    }

    @Test
    public void testSetWifeException() {
        assertThrows(IllegalArgumentException.class, () -> {
            family.setWife(null);
        });
    }

    @Test
    public void testSetWifeLogMessage() {
        family.setWife(Individual.builder().male().build());

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US21", Level.ERROR, 21));
    }

    @Test
    public void testMarriage() {
        Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date divorce = DateBuilder.build(1, Calendar.JANUARY, 2001);
        assertEquals(marriage, Family.builder().marriage(marriage).build().getMarriage());
        assertEquals(marriage, Family.builder().divorce(marriage).marriage(marriage).build().getMarriage());
        assertEquals(marriage, Family.builder().divorce(divorce).marriage(marriage).build().getMarriage());
    }

    @Test
    public void testMarriageException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Family family = Family.builder().build();
            family.setMarriage(null);
        });
    }

    @Test
    public void testMarriageLogMessage1() {
        Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2001);
        Date divorce = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Family family = Family.builder().divorce(divorce).build();
        family.setMarriage(marriage);

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US04", Level.ERROR, 4));
    }
    
    @Test
    public void testMarriageLogMessage2() {
        Date now = new Date();
        Date marriage = DateBuilder.newDateDaysAfter(now, 1);
        Family family = Family.builder().marriage(marriage).build();

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US01", Level.ERROR, 1));
    }

    @Test
    public void testDivorce() {
        Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Date divorce = DateBuilder.build(1, Calendar.JANUARY, 2001);
        assertEquals(divorce, Family.builder().divorce(divorce).build().getDivorce());
        assertEquals(divorce, Family.builder().marriage(divorce).divorce(divorce).build().getDivorce());
        assertEquals(divorce, Family.builder().marriage(marriage).divorce(divorce).build().getDivorce());
    }

    @Test
    public void testDivorceExcpetion() {
        assertThrows(IllegalArgumentException.class, () -> {
            Family family = Family.builder().build();
            family.setDivorce(null);
        });
    }

    @Test
    public void testDivorceLogMessage1() {
        Date marriage = DateBuilder.build(1, Calendar.JANUARY, 2001);
        Date divorce = DateBuilder.build(1, Calendar.JANUARY, 2000);
        Family family = Family.builder().marriage(marriage).build();
        family.setDivorce(divorce);

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US04", Level.ERROR, 4));
    }
    @Test
    public void testDivorceLogMessage2() {
        Date now = new Date();
        Date marriage = DateBuilder.newDateDaysAfter(now, 1);
        Family family = Family.builder().divorce(marriage).build();

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US01", Level.ERROR, 1));
    }

    @Test
    public void testAddChildren() {
        for (int i = 0; i < Family.MAX_CHILDREN; i++) {
            assertTrue(family.addChild(Individual.builder().build()));
        }
    }

    @Test
    public void testAddChildrenExcpetion() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            family.addChild(null);
        });

        String expectedMessage = "Child cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddChildrenLogMessage() {
        for (int i = 0; i < Family.MAX_CHILDREN + 1; i++) {
            Individual child = Individual.builder().build();
            assertTrue(family.addChild(child));
        }

        assertEquals(1, logAppender.countEvents());
        assertTrue(logAppender.contains("US15", Level.ANOMALY, 15));
    }

    @Test
    public void testGetChildren() {
        Individual child1 = Individual.builder().birth(1, Calendar.JANUARY, 2000).build();
        Individual child2 = Individual.builder().birth(1, Calendar.JANUARY, 2001).build();
        Individual child3 = Individual.builder().birth(1, Calendar.JANUARY, 2002).build();
        Individual child4 = Individual.builder().birth(1, Calendar.JANUARY, 1999).build();
        Individual child5 = Individual.builder().birth(1, Calendar.JANUARY, 1998).build();

        Family family = Family.builder().children(child1, child2, child3, child4, child5).build();
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
