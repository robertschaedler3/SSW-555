package project;

import project.models.ParsedGED;

public class NoBigamy {
	public static void check(String path) {
        ParsedGED gedData = new ParsedGED(path);
        System.out.println(gedData.getIndividuals().size());
        System.out.println(gedData.getFamilies().size());
	}
}
