package gedcom;

import gedcom.interfaces.Gender;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import gedcom.validators.DefaultValidator;
import gedcom.validators.ValidBirth;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class TestValidBirth {

    private GEDFile gedfile;
    private Family family;

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
            Individual mother = makeIndividual(MOTHER_ID, dateFmt.parse(MOTHER_BIRTH), dateFmt.parse(MOTHER_DEATH),
                    Gender.F);
            Individual father = makeIndividual(FATHER_ID, dateFmt.parse(FATHER_BIRTH), dateFmt.parse(FATHER_DEATH),
                    Gender.M);
            Individual child = makeIndividual(CHILD_ID, dateFmt.parse(childBirthday), null, Gender.M);

            Family family = new Family("Family");
            family.setWife(MOTHER_ID);
            family.setHusband(FATHER_ID);
            family.setMarriage(dateFmt.parse(marriage));
            family.addChild(CHILD_ID);

            this.family = family;

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

    @Test
    public void testSiblingValidSpacing() {
        try {
            buildGedfile("01 JAN 2000", MARRIAGE);

            Map<String, Individual> individuals = this.gedfile.getIndividuals();

            Individual mother = individuals.get(MOTHER_ID);
            Individual father = individuals.get(FATHER_ID);
            Individual child = individuals.get(CHILD_ID);

            Individual sibling1 = makeIndividual("SIBLING_1", dateFmt.parse("01 DEC 2000"), null, Gender.M);
            Individual sibling2 = makeIndividual("SIBLING_2", dateFmt.parse("02 JAN 2000"), null, Gender.M);
            this.family.addChild("SIBLING_1");
            this.family.addChild("SIBLING_2");

            this.gedfile = new GEDFile(new Individual[] { mother, father, child, sibling1, sibling2 },
                    new Family[] { this.family });

            assertTrue(this.validator.isValid(this.gedfile));
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void testSiblingInvalidSpacing() {
        try {
            buildGedfile("01 JAN 2000", MARRIAGE);

            Map<String, Individual> individuals = this.gedfile.getIndividuals();

            Individual mother = individuals.get(MOTHER_ID);
            Individual father = individuals.get(FATHER_ID);
            Individual child = individuals.get(CHILD_ID);

            Individual sibling1 = makeIndividual("SIBLING_1", dateFmt.parse("01 MAR 2000"), null, Gender.M);
            Individual sibling2 = makeIndividual("SIBLING_2", dateFmt.parse("01 JUN 2000"), null, Gender.M);
            this.family.addChild("SIBLING_1");
            this.family.addChild("SIBLING_2");

            this.gedfile = new GEDFile(new Individual[] { mother, father, child, sibling1, sibling2 },
                    new Family[] { this.family });

            assertFalse(this.validator.isValid(this.gedfile));
        } catch (ParseException e) {
            fail();
        }
    }

}
