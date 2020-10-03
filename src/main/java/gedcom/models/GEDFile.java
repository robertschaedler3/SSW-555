package gedcom.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import gedcom.interfaces.Gender;
import gedcom.interfaces.Tag;

public class GEDFile {

    private Map<String, Individual> individuals;
    private Map<String, Family> families;;

    public GEDFile(Individual[] indivs, Family[] fams) {
        individuals = new HashMap<String, Individual>();
        families = new HashMap<String, Family>();

        for (Individual indi : indivs) {
            individuals.put(indi.getID(), indi);
        }
        for (Family fam : fams) {
            families.put(fam.getID(), fam);
        }
    }

    public GEDFile(File file) {
        individuals = new HashMap<String, Individual>();
        families = new HashMap<String, Family>();

        Scanner s = null;

        try {
            s = new Scanner(file);
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
                    individuals.put(currentLine.getID(), parseIndividual(gedLines, i, currentLine.getID()));
                } else if (currentLine.getTag() == Tag.FAM) {
                    families.put(currentLine.getID(), parseFamily(gedLines, i, currentLine.getID()));
                }
            }

            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("The file you entered could not be found.");
            System.exit(1);
        }

    }

    private Individual parseIndividual(List<GEDLine> list, int index, String ID) {
        Individual indi = new Individual(ID);
        Tag dateType = null;

        for (int i = index + 1; i < list.size(); i++) {
            GEDLine gedLine = list.get(i);
            if (gedLine.getLevel() == 0) {
                break;
            }

            switch (gedLine.getTag()) {
                case NAME:
                    indi.setName(gedLine.getArgs());
                    break;
                case SEX:
                    indi.setGender(Gender.valueOf(gedLine.getArgs()));
                    break;
                case BIRT:
                    dateType = Tag.BIRT;
                    break;
                case DEAT:
                    dateType = Tag.DEAT;
                    break;
                case FAMC:
                    indi.addChildFamily(gedLine.getArgs());
                    break;
                case FAMS:
                    indi.addSpouseFamily(gedLine.getArgs());
                    break;
                default:
                    break;
            }

            if (gedLine.getTag() == Tag.DATE && dateType != null) {
                DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                try {
                    Date date = formatter.parse(gedLine.getArgs());
                    if (dateType == Tag.BIRT) {
                        indi.setBirthday(date);
                    } else if (dateType == Tag.DEAT) {
                        indi.setDeath(date);
                    }
                    dateType = null;
                } catch (ParseException e) {
                    System.out.println(String.format("Error parsing date: \"%s\"", gedLine.getArgs()));
                }
            }

        }
        return indi;
    }

    private Family parseFamily(List<GEDLine> list, int index, String ID) {
        Family fam = new Family(ID);
        Tag dateType = null;

        for (int i = index + 1; i < list.size(); i++) {
            GEDLine gedLine = list.get(i);
            if (gedLine.getLevel() == 0) {
                break;
            }

            switch (gedLine.getTag()) {
                case MARR:
                    dateType = Tag.MARR;
                    break;
                case DIV:
                    dateType = Tag.DIV;
                    break;
                case HUSB:
                    fam.setHusband(gedLine.getArgs());
                    break;
                case WIFE:
                    fam.setWife(gedLine.getArgs());
                    break;
                case CHIL:
                    fam.addChild(gedLine.getArgs());
                    break;
                default:
                    break;
            }

            if (gedLine.getTag() == Tag.DATE && dateType != null) {
                DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                try {
                    Date date = formatter.parse(gedLine.getArgs());
                    if (dateType == Tag.MARR) {
                        fam.setMarriage(date);
                    } else if (dateType == Tag.DIV) {
                        fam.setDivorce(date);
                    }
                    dateType = null;
                } catch (ParseException e) {
                    System.out.println(String.format("Error parsing date: \"%s\"", gedLine.getArgs()));
                }
            }
        }

        return fam;
    }

    public Table getIndividualsTable() {
        List<String> headers = Arrays.asList("ID", "Gender", "Name", "Birthday", "Age", "Alive", "Death", "Child",
                "Spouse");
        List<List<String>> rows = new ArrayList<>();
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-mm-dd");

        for (Map.Entry<String, Individual> entry : individuals.entrySet()) {
            Individual indi = entry.getValue();
            rows.add(Arrays.<String>asList(indi.getID(), indi.getGender().toString(), indi.getName(),
                    (indi.getBirthday() != null) ? dateFmt.format(indi.getBirthday()) : "NA",
                    (indi.getBirthday() != null) ? Long.toString(indi.age()) : "NA",
                    (indi.getBirthday() != null) ? Boolean.toString(indi.alive()) : "NA",
                    (indi.getDeath() != null) ? dateFmt.format(indi.getDeath()) : "NA",
                    indi.getChildrenFamily().toString(), indi.getSpouseFamily().toString()));
        }

        return new Table(headers, rows);
    }

    public Table getFamiliesTable() {
        List<String> headers = Arrays.asList("ID", "Married", "Divorced", "Husband ID", "Husband Name", "Wife ID",
                "Wife Name", "Children");
        List<List<String>> rows = new ArrayList<>();
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-mm-dd");

        for (Map.Entry<String, Family> entry : families.entrySet()) {
            Family fam = entry.getValue();
            rows.add(Arrays.<String>asList(fam.getID(),
                    (fam.getMarriage() != null) ? dateFmt.format(fam.getMarriage()) : "NA",
                    (fam.getDivorce() != null) ? dateFmt.format(fam.getDivorce()) : "NA", fam.getHusband(),
                    (fam.getHusband() != "NA") ? individuals.get(fam.getHusband()).getName() : "NA", fam.getWife(),
                    (fam.getWife() != "NA") ? individuals.get(fam.getWife()).getName() : "NA",
                    fam.getChildren().toString()));
        }

        return new Table(headers, rows);
    }

    public Map<String, Individual> getIndividuals() {
        return individuals;
    }

    public Map<String, Family> getFamilies() {
        return families;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nIndividuals");
        sb.append(getIndividualsTable());
        sb.append("Families");
        sb.append(getFamiliesTable());
        return sb.toString();
    }
    
    public Individual getIndividualById(String id) {
    	for(Individual indi : individuals.values()) {
    		if(indi.getID().equals(id)) return indi;
    	}
    	return null;
    }

}
