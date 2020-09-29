package project.tests;

import project.Validator;
import project.interfaces.Gender;
import project.models.Family;
import project.models.GEDFile;
import project.models.Individual;
import project.validators.DefaultValidator;
import project.validators.ValidBirth;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestValidBirth {

    private GEDFile gedfile;
    private Validator validator = new DefaultValidator();
    private DateFormat dateFmt;

    private final String DATE_FORMAT = "dd MMM yyyy";

    private final String MOTHER_ID = "MOTHER";
    private final String MOTHER_BIRTH = "15 AUG 1950";
    private final String MOTHER_DEATH = "15 AUG 2010";

    private final String FATHER_ID = "FATHER";
    private final String FATHER_BIRTH = "10 MAR 1950";
    private final String FATHER_DEATH = "10 MAR 2010";

    private final String MARRIAGE = "01 JAN 1990";

    private final String CHILD_ID = "CHILD";

    public TestValidBirth() {
        this.dateFmt = new SimpleDateFormat(DATE_FORMAT);
        this.validator = new ValidBirth(this.validator);
    }

    private void buildGedfile(String childBirthday, String marriage) {
        try {
            Individual mother = makeIndividual(MOTHER_ID, dateFmt.parse(MOTHER_BIRTH), dateFmt.parse(MOTHER_DEATH), Gender.F);
            Individual father = makeIndividual(FATHER_ID, dateFmt.parse(FATHER_BIRTH), dateFmt.parse(FATHER_DEATH), Gender.M);
            Individual child = makeIndividual(CHILD_ID, dateFmt.parse(childBirthday), null, Gender.M);
            
            Family family = new Family("Family");
            family.setWife(MOTHER_ID);
            family.setHusband(FATHER_ID);
            family.setMarriage(dateFmt.parse(marriage));
            family.addChild(CHILD_ID);
            
            this.gedfile = new GEDFile(new Individual[] { mother, father, child }, new Family[] { family });

        } catch (Exception e) {
            System.out.println("Error building GEDFile.");
            fail();
        }
    }
    
    private Individual makeIndividual(String ID, Date birthday, Date death, Gender gender) {
        Individual individual = new Individual(ID);
        individual.setBirthday(birthday);
        individual.setDeath(death);
        individual.setGender(gender);
        return individual;
    }
    
    @Test
    public void testValidBirth() {
        buildGedfile("01 JAN 2000", MARRIAGE);
        assertTrue(this.validator.isValid(this.gedfile));
    }
    
    @Test
    public void testBirthAfterFatherDeath() {
        // Birth after death, before 9 months
        buildGedfile("10 MAY 2010", MARRIAGE);
        System.out.println();
        assertTrue(this.validator.isValid(this.gedfile));

        // Birth after death, after 9 months
        buildGedfile("10 MAR 2011", MARRIAGE);
        assertFalse(this.validator.isValid(this.gedfile));
    }
    
    @Test
    public void testBirthAfterMotherDeath() {
        buildGedfile("16 AUG 2010", MARRIAGE);
        assertFalse(this.validator.isValid(this.gedfile));
    }
    
    @Test
    public void testBirthBeforeMarriage() {
        buildGedfile("01 JAN 1980", MARRIAGE);
        assertFalse(this.validator.isValid(this.gedfile));
    }
    

}
