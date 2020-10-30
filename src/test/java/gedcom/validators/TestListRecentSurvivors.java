package gedcom.validators;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import gedcom.GEDParser;
import gedcom.builders.FamilyBuilder;
import gedcom.builders.GEDFileBuilder;
import gedcom.builders.IndividualBuilder;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

class TestListRecentSurvivors {
	
	private Date getCurrentDateWithOffset(int offset) {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, offset);
	    return cal.getTime();
	}

	@Test
	public void testHasRecentDeath(){
		Date yesterday = getCurrentDateWithOffset(-1);
		
		Individual husband1 = new IndividualBuilder().male().withDeath(yesterday).build();
        Individual wife1 = new IndividualBuilder().female().build();
        
        Individual child = new IndividualBuilder().male().build();
        
        Family family1 = new FamilyBuilder().withHusband(husband1).withWife(wife1).withChild(child).build();

        GEDFile gedFile = new GEDFileBuilder().withIndividuals(husband1, wife1, child).withFamilies(family1).build();
        
        assertTrue(GEDParser.US37ListRecentSurvivors(gedFile).size() == 1);
	}
	
	@Test
	public void testHasMultipleRecentDeaths(){
		Date yesterday = getCurrentDateWithOffset(-1);
		
		Individual husband1 = new IndividualBuilder().male().withDeath(yesterday).build();
        Individual wife1 = new IndividualBuilder().female().build();
        Individual child1 = new IndividualBuilder().male().build();
        Family family1 = new FamilyBuilder().withHusband(husband1).withWife(wife1).withChild(child1).build();

		Individual husband2 = new IndividualBuilder().male().withDeath(yesterday).build();
        Individual wife2 = new IndividualBuilder().female().build();
        Individual child2 = new IndividualBuilder().male().build();
        Individual child3 = new IndividualBuilder().male().build();
        Family family2 = new FamilyBuilder().withHusband(husband2).withWife(wife2).withChild(child2).withChild(child3).build();
        GEDFile gedFile = new GEDFileBuilder().withIndividuals(husband1, wife1, child1, husband2, wife2, child2, child3).withFamilies(family1, family2).build();
        
        assertTrue(GEDParser.US37ListRecentSurvivors(gedFile).size() == 2);
	}
	
	@Test
	public void testNoRecentDeaths(){
		Date yesterday = getCurrentDateWithOffset(-45);
		
		Individual husband1 = new IndividualBuilder().male().withDeath(yesterday).build();
        Individual wife1 = new IndividualBuilder().female().build();
        
        Individual child = new IndividualBuilder().male().build();
        
        Family family1 = new FamilyBuilder().withHusband(husband1).withWife(wife1).withChild(child).build();

        GEDFile gedFile = new GEDFileBuilder().withIndividuals(husband1, wife1, child).withFamilies(family1).build();
        
        assertTrue(GEDParser.US37ListRecentSurvivors(gedFile).size() == 0);
	}

}
