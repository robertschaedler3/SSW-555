package gedcom.models;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.Test;

import gedcom.builders.FamilyBuilder;
import gedcom.builders.GEDCOMBuilder;
import gedcom.builders.IndividualBuilder;

public class TestGEDFile {

    @Test
    public void testConstructorScanner() {
        GEDCOMBuilder gedcomBuilder = new GEDCOMBuilder();
        FamilyBuilder familyBuilder = new FamilyBuilder();
        IndividualBuilder individualBuilder = new IndividualBuilder();

        Individual husband = individualBuilder.male().withBirth(1900).withDeath(1980).build();
        Individual wife = individualBuilder.female().withBirth(1900).withDeath(1980).build();

        familyBuilder.withSpouses(husband, wife).withMarriage(1930).withDivorce(1970).build();

        List<Individual> individuals = individualBuilder.getIndividuals();
        List<Family> families = familyBuilder.getFamilies();

        gedcomBuilder.withIndividuals(individuals);
        gedcomBuilder.withFamilies(families);

        String GEDCOM = gedcomBuilder.build();
        try {
            GEDFile gedFile = new GEDFile(GEDCOMBuilder.getTempFile(GEDCOM));
            assertEquals(families.size(), gedFile.getFamilies().size());
            assertEquals(individuals.size(), gedFile.getIndividuals().size());            
        } catch (FileNotFoundException e) {
            fail();
        }

        // TODO: check list contents for field equality
    }

}
