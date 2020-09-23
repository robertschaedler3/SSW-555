package project.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import project.interfaces.Tag;
import project.GEDParser;

public class ParsedGED {
	private List<Individual> individuals;
	private List<Family> families;

	public ParsedGED(String path) {
		super();

		ArrayList<Individual> newIndividuals = new ArrayList<Individual>();
		ArrayList<Family> newFamilies = new ArrayList<Family>();
		
		try {
			File text = new File(path);
			Scanner s;
			s = new Scanner(text);
			String line;
			List<GEDLine> gedLines = new ArrayList<>();

			// Read file into GEDLine list
			while (s.hasNextLine()) {
				line = s.nextLine();
				gedLines.add(new GEDLine(line));
			}

			// Create all the Individuals and Families
			for (int i = 0; i < gedLines.size(); i++) {
				GEDLine currentLine = gedLines.get(i);
				if (currentLine.getTag() == Tag.INDI) {
					newIndividuals.add(GEDParser.parseIndividual(gedLines, i, currentLine.getID()));
				} else if (currentLine.getTag() == Tag.FAM) {
					newFamilies.add(GEDParser.parseFamily(gedLines, i, currentLine.getID()));
				}
			}

			s.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("The file you entered could not be found.");
			System.exit(1);
		}
		
		this.individuals = newIndividuals;
		this.families = newFamilies;
	}

	public List<Individual> getIndividuals() {
		return individuals;
	}

	public void setIndividuals(List<Individual> individuals) {
		this.individuals = individuals;
	}

	public List<Family> getFamilies() {
		return families;
	}

	public void setFamilies(List<Family> families) {
		this.families = families;
	}
}
