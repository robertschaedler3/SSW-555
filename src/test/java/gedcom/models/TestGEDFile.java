package gedcom.models;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestGEDFile {

    @Test
    public void testConstructorScanner() {
        GEDFile.Writer gedfileWriter = new GEDFile.Writer();
        Family.Builder familyBuilder = Family.builder();
        Individual.Builder individualBuilder = Individual.builder();

        Individual husband = individualBuilder.male().birth(1900).death(1980).build();
        Individual wife = individualBuilder.female().birth(1900).death(1980).build();

        familyBuilder.spouses(husband, wife).marriage(1930).divorce(1970).build();

        List<Individual> individuals = individualBuilder.getIndividuals();
        List<Family> families = familyBuilder.getFamilies();

        gedfileWriter.individuals(individuals);
        gedfileWriter.families(families);

        String GEDCOM = gedfileWriter.build();
        try {
            GEDFile gedFile = new GEDFile(GEDFile.Writer.getTempFile(GEDCOM));
            assertEquals(families.size(), gedFile.getFamilies().size());
            assertEquals(individuals.size(), gedFile.getIndividuals().size());            
        } catch (FileNotFoundException e) {
            fail();
        }

        // TODO: check list contents for field equality
    }

}
