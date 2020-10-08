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
            GEDLine currentLine;

            // Read file into GEDLine list
            while (s.hasNextLine()) {
                line = s.nextLine();
                currentLine = new GEDLine(line);
                gedLines.add(currentLine);

                // Create 'blank' Individuals and Families
                if (currentLine.getTag() == Tag.INDI) {
                    individuals.put(currentLine.getID(), new Individual(currentLine.getID()));
                } else if (currentLine.getTag() == Tag.FAM) {
                    families.put(currentLine.getID(), new Family(currentLine.getID()));
                }
            }

            // Populate Individual and Family fields
            for (int i = 0; i < gedLines.size(); i++) {
                currentLine = gedLines.get(i);
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
        Individual individual = this.individuals.get(ID);
        Tag dateType = null;

        for (int i = index + 1; i < list.size(); i++) {
            GEDLine gedLine = list.get(i);
            if (gedLine.getLevel() == 0) {
                break;
            }

            switch (gedLine.getTag()) {
                case NAME:
                    individual.setName(gedLine.getArgs());
                    break;
                case SEX:
                    individual.setGender(Gender.valueOf(gedLine.getArgs()));
                    break;
                case BIRT:
                    dateType = Tag.BIRT;
                    break;
                case DEAT:
                    dateType = Tag.DEAT;
                    break;
                case FAMC:
                    individual.addChildFamily(this.families.get(gedLine.getArgs()));
                    break;
                case FAMS:
                    individual.addSpouseFamily(this.families.get(gedLine.getArgs()));
                    break;
                default:
                    break;
            }

            if (gedLine.getTag() == Tag.DATE && dateType != null) {
                DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                try {
                    Date date = formatter.parse(gedLine.getArgs());
                    if (dateType == Tag.BIRT) {
                        individual.setBirthday(date);
                    } else if (dateType == Tag.DEAT) {
                        individual.setDeath(date);
                    }
                    dateType = null;
                } catch (ParseException e) {
                    System.out.println(String.format("Error parsing date: \"%s\"", gedLine.getArgs()));
                }
            }

        }
        return individual;
    }

    private Family parseFamily(List<GEDLine> list, int index, String ID) {
        Family family = this.families.get(ID);
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
                    family.setHusband(this.individuals.get(gedLine.getArgs()));
                    break;
                case WIFE:
                    family.setWife(this.individuals.get(gedLine.getArgs()));
                    break;
                case CHIL:
                    family.addChild(this.individuals.get(gedLine.getArgs()));
                    break;
                default:
                    break;
            }

            if (gedLine.getTag() == Tag.DATE && dateType != null) {
                DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                try {
                    Date date = formatter.parse(gedLine.getArgs());
                    if (dateType == Tag.MARR) {
                        family.setMarriage(date);
                    } else if (dateType == Tag.DIV) {
                        family.setDivorce(date);
                    }
                    dateType = null;
                } catch (ParseException e) {
                    System.out.println(String.format("Error parsing date: \"%s\"", gedLine.getArgs()));
                }
            }
        }

        return family;
    }

    public Table getIndividualsTable() {
        List<String> headers = Arrays.asList("ID", "Gender", "Name", "Birthday", "Age", "Alive", "Death", "Child",
                "Spouse");
        List<List<String>> rows = new ArrayList<>();
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-mm-dd");

        for (Map.Entry<String, Individual> entry : individuals.entrySet()) {
            Individual individual = entry.getValue();
            rows.add(Arrays.<String>asList(individual.getID(), individual.getGender().toString(), individual.getName(),
                    (individual.getBirthday() != null) ? dateFmt.format(individual.getBirthday()) : "NA",
                    (individual.getBirthday() != null) ? Long.toString(individual.age()) : "NA",
                    (individual.getBirthday() != null) ? Boolean.toString(individual.alive()) : "NA",
                    (individual.getDeath() != null) ? dateFmt.format(individual.getDeath()) : "NA",
                    individual.getChildrenFamily().toString(), individual.getSpouseFamily().toString()));
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
                    (fam.getDivorce() != null) ? dateFmt.format(fam.getDivorce()) : "NA", fam.getHusband().getName(),
                    (fam.getHusband() != null) ? fam.getHusband().getID() : "NA", fam.getWife().getName(),
                    (fam.getWife() != null) ? fam.getWife().getID() : "NA", fam.getChildren().toString()));
        }

        return new Table(headers, rows);
    }

    public List<Individual> getIndividuals() {
        return new ArrayList<Individual>(this.individuals.values());
    }

    public Individual getIndividual(String ID) {
        return this.individuals.get(ID);
    }

    public List<Family> getFamilies() {
        return new ArrayList<Family>(this.families.values());
    }

    public Family getFamily(String ID) {
        return this.families.get(ID);
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

}
