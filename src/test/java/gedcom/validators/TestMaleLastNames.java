package gedcom.validators;

import gedcom.interfaces.Gender;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestMaleLastNames {

    Validator validator = new DefaultValidator();
    Validator maleLastNamesValidator = new MaleLastNames(validator);

    Individual dad = new Individual("dad");
    Individual child1 = new Individual("child1");
    Individual child2 = new Individual("child2");

    Individual[] individuals = {dad, child1, child2};

    Family family = new Family("Family");
    Family[] families = {family};

    public void setUpFamily() {
        family.setHusband(dad);
        dad.setName("John /Smith/");
        dad.addSpouseFamily(family);
        dad.setGender(Gender.M);

        child1.addChildFamily(family);
        child2.addChildFamily(family);
        family.addChild(child1);
        family.addChild(child2);
    }

    @Test
    public void checkValidSons() {
        setUpFamily();

        child1.setGender(Gender.M);
        child1.setName("Joe /Smith/");

        child2.setGender(Gender.M);
        child2.setName("Bob /Smith/");

        assertTrue(maleLastNamesValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkValidDaughters() {
        setUpFamily();

        child1.setGender(Gender.F);
        child1.setName("Jane /Vesonder/");

        child2.setGender(Gender.F);
        child2.setName("Connie /Rowland/");

        assertTrue(maleLastNamesValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkInvalidSonAndDaughter() {
        setUpFamily();

        child1.setGender(Gender.M);
        child1.setName("Joe /Vesonder/");


        child2.setGender(Gender.F);
        child2.setName("Connie /Rowland/");

        assertFalse(maleLastNamesValidator.isValid((new GEDFile(individuals, families))));
    }
}
