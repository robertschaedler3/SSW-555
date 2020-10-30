package gedcom.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import java.time.temporal.ChronoUnit;

public class ListRecentSurvivors extends Validator {

	public ListRecentSurvivors(Validator validator) {
		super(validator);
	}

	protected boolean check(GEDFile gedFile) {
		boolean valid = true;
		
		List<Individual> recentlyDead = new ArrayList<Individual>();
		
		for(Individual indi : gedFile.getIndividuals()) {
			if(indi.getDeath() != null
					&& ChronoUnit.DAYS.between(
							indi.getDeath().toInstant(),
							(new Date()).toInstant()) < 30) {
				recentlyDead.add(indi);
				
			}
		}
		
		for(Individual deadIndiv : recentlyDead){	
			for(Family fam : gedFile.getFamilies()) {
				if(fam.getHusband().getID() == deadIndiv.getID()) {
					Collection<Individual> survivors = new ArrayList<Individual>();
					System.out.printf("List US37: %s (individual %s) passed away in the last 30 days (%s). "
							+ "They are survived by wife %s (%s) and descendants: ", deadIndiv.getName(), deadIndiv.getID(), deadIndiv.getDeath().toString(), fam.getWife().getName(),  fam.getWife().getID());
					for(Individual desc : deadIndiv.getDescendants()) {
						System.out.printf("%s (%s),", desc.getName(), desc.getID());
					}
					
				}
				else if(fam.getWife().getID() == deadIndiv.getID()) {
					Collection<Individual> survivors = new ArrayList<Individual>();
					System.out.printf("List US37: %s (individual %s) passed away in the last 30 days (%s). "
							+ "They are survived by husband %s (%s) and descendants: ", deadIndiv.getName(), deadIndiv.getID(), deadIndiv.getDeath().toString(), fam.getHusband().getID(), fam.getHusband().getID());
					for(Individual desc : deadIndiv.getDescendants()) {
						System.out.printf("%s (%s),", desc.getName(), desc.getID());
					}
					
				}
			}
		}

		return valid;
	}

}
