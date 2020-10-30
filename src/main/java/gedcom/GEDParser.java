package gedcom;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import gedcom.validators.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class GEDParser {

	private static GEDFile gedFile;
	private static Validator validator = new DefaultValidator();

	private static void parseArgs(String[] args) {
		for (int i = 1; i < args.length; i++) {
			switch (args[i]) {
			case "--multiple-births":
				validator = new MultipleBirths(validator);
				break;
			case "--divorce-before-death":
				validator = new DivorceBeforeDeath(validator);
				break;
			case "--no-bigamy":
				validator = new NoBigamy(validator);
				break;
			case "--no-incest":
				validator = new NoIncest(validator);
				break;
			case "--valid-birth":
				validator = new ValidBirth(validator);
				break;
			case "--valid-marriage":
				validator = new ValidMarriage(validator);
				break;
			case "--parents-not-too-old":
				validator = new ParentsNotTooOld(validator);
				break;
			case "--corresponding-entries":
				validator = new CorrespondingEntries(validator);
				break;
			case "--unique-first-names":
				validator = new UniqueNameBirthdays(validator);
				break;
			case "--male-last-names":
				validator = new MaleLastNames(validator);
				break;
			case "--no-marriages-to-descendants":
				validator = new NoMarriagesToDescendants(validator);
				break;
			case "--list-recent-survivors":
				US37ListRecentSurvivors(gedFile);
				break;
			case "--tabulate":
				System.out.println(gedFile);
				break;
			default:
				break;
			}
		}
	}

	public static Collection<Individual> US37ListRecentSurvivors(GEDFile gedFile) {
		List<Individual> recentlyDead = new ArrayList<Individual>();

		for (Individual indi : gedFile.getIndividuals()) {
			if (indi.getDeath() != null
					&& ChronoUnit.DAYS.between(indi.getDeath().toInstant(), (new Date()).toInstant()) < 30) {
				recentlyDead.add(indi);
			}
		}

		for (Individual deadIndiv : recentlyDead) {
			for (Family fam : gedFile.getFamilies()) {
				if (fam.getHusband().getID() == deadIndiv.getID()) {
					Collection<Individual> survivors = new ArrayList<Individual>();
					survivors.add(fam.getWife());
					survivors.addAll(deadIndiv.getDescendants());
					System.out.printf("List US37: %s (%s) died in the last 30 days, leaving spouse and descendants:\n", fam.getWife().getName(), fam.getWife().getID());
					System.out.print(GEDFile.getIndividualsTable(survivors).toString());
				} else if (fam.getWife().getID() == deadIndiv.getID()) {
					Collection<Individual> survivors = new ArrayList<Individual>();
					survivors.add(fam.getHusband());
					survivors.addAll(deadIndiv.getDescendants());
					System.out.printf("List US37: %s (%s) died in the last 30 days, leaving spouse and descendants:\n", fam.getHusband().getName(), fam.getHusband().getID());
					System.out.print(GEDFile.getIndividualsTable(survivors).toString());
				}
			}
		}
		
		return recentlyDead;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: ./GEDParser <.ged file> [ARGS]");
			return;
		}

		File file;
		Scanner scanner;

		try {
			file = new File(args[0]);
			scanner = new Scanner(file);
			gedFile = new GEDFile(scanner);

			parseArgs(args);

			scanner.close();

			boolean valid = validator.isValid(gedFile);
			System.out.println(String.format("gedcom is %s", valid ? "VALID" : "INVALID"));
		} catch (FileNotFoundException fe) {
			System.out.println("The file you entered could not be found.");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

}
