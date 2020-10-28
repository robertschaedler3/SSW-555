package gedcom.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import gedcom.interfaces.Gender;
import gedcom.interfaces.Tag;

public class GEDFile {

    private static int TOSTRING_LIST_LENGTH = 75;

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

    public GEDFile(List<Individual> indivs, List<Family> fams) {
        individuals = new HashMap<String, Individual>(indivs.stream().collect(Collectors.toMap(Individual::getID, individual -> individual)));
        families = new HashMap<String, Family>(fams.stream().collect(Collectors.toMap(Family::getID, family -> family)));
    }

    public GEDFile(Scanner s) {
        individuals = new HashMap<String, Individual>();
        families = new HashMap<String, Family>();

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
                if (individuals.put(currentLine.getID(), new Individual(currentLine.getID())) != null) {
                    s.close();
                    throw new IllegalStateException(String.format("Error US15: cannot create individual with duplicate ID %s.", currentLine.getID()));
                }
            } else if (currentLine.getTag() == Tag.FAM) {
                if (families.put(currentLine.getID(), new Family(currentLine.getID())) != null) {
                    s.close();
                    throw new IllegalStateException(String.format("Error US15: cannot create family with duplicate ID %s.", currentLine.getID()));
                }
            }
        }

        // Populate Individual and Family fields
        for (int i = 0; i < gedLines.size(); i++) {
            currentLine = gedLines.get(i);
            if (currentLine.getTag() == Tag.INDI) {
                parseIndividual(gedLines, i, currentLine.getID());
            } else if (currentLine.getTag() == Tag.FAM) {
                parseFamily(gedLines, i, currentLine.getID());
            }
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

    private <T> void addTruncatedList(List<T> items, StringBuilder sb) {
        int i = 0;
        int length = sb.length();

        sb.append("[ ");
        sb.append(items.get(i++));

        while (i < items.size()) {
            if (items.get(i) == null) {
                sb.append(", null");
                i++;
            } else if (sb.length() + (items.get(i).toString().length() - length) > TOSTRING_LIST_LENGTH) {
                break;
            } else {
                sb.append(", ");
                sb.append(items.get(i++));
            }
        }

        if (i != items.size()) {
            sb.append(" ...");
        }

        sb.append(" ]");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nIndividuals\n");
        addTruncatedList(getIndividuals(), sb);
        sb.append("\nFamilies\n");
        addTruncatedList(getFamilies(), sb);
        sb.append("\n");
        return sb.toString();
    }

}
