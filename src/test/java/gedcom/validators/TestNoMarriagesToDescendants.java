package gedcom.validators;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestNoMarriagesToDescendants {

    Validator validator = new DefaultValidator();
    Validator noMarriagesToDescendantsValidator = new NoMarriagesToDescendants(validator);

    Individual parent1 = new Individual("parent1");
    Individual parent2 = new Individual("parent2");
    Individual child = new Individual("child");
    Individual[] individuals = {parent1, parent2};

    Family generation1 = new Family("generation1");
    Family[] families = {generation1};

    @Test
    public void checkValid() {
        generation1.setHusband(parent1);
        generation1.setWife(parent2);
        parent1.addSpouseFamily(generation1);
        parent2.addSpouseFamily(generation1);

        generation1.addChild(child);
        child.addChildFamily(generation1);

        assertTrue(noMarriagesToDescendantsValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkInvalidHusband() {
        generation1.setWife(parent1);
        parent1.addSpouseFamily(generation1);

        generation1.addChild(child);
        child.addChildFamily(generation1);

        generation1.setHusband(child);

        assertFalse(noMarriagesToDescendantsValidator.isValid((new GEDFile(individuals, families))));
    }

    @Test
    public void checkInvalidWife() {
        generation1.setHusband(parent1);
        parent1.addSpouseFamily(generation1);

        generation1.addChild(child);
        child.addChildFamily(generation1);

        generation1.setWife(child);
        assertFalse(noMarriagesToDescendantsValidator.isValid((new GEDFile(individuals, families))));
    }

}
