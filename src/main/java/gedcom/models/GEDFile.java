package gedcom.models;

import java.io.File;
import java.io.FileNotFoundException;
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
import gedcom.logging.Error;
import gedcom.logging.Logger;

public class GEDFile {

    private static int TOSTRING_LIST_LENGTH = 75;
    public static final String GEDCOM_DATE_FORMAT = "dd MMM yyyy";

    private Map<String, Individual> individuals;
    private Map<String, Family> families;

    private Logger LOGGER = Logger.getInstance();

    public GEDFile(Individual[] individuals, Family[] families) {
        this.individuals = new HashMap<String, Individual>();
        this.families = new HashMap<String, Family>();

        for (Individual individual : individuals) {
            this.individuals.put(individual.getID(), individual);
        }
        for (Family family : families) {
            this.families.put(family.getID(), family);
        }
    }

    public GEDFile(List<Individual> individuals, List<Family> families) {
        this.individuals = new HashMap<String, Individual>(individuals.stream().collect(Collectors.toMap(Individual::getID, individual -> individual)));
        this.families = new HashMap<String, Family>(families.stream().collect(Collectors.toMap(Family::getID, family -> family)));
    }

    public GEDFile(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        individuals = new HashMap<String, Individual>();
        families = new HashMap<String, Family>();

        String line;
        List<GEDLine> gedLines = new ArrayList<>();
        GEDLine currentLine;

        // Read file into GEDLine list
        int n = 0;
        while (s.hasNextLine()) {

            Logger.setLineContext(++n);

            line = s.nextLine();
            currentLine = new GEDLine(line);
            gedLines.add(currentLine);

            // Create 'blank' Individuals and Families
            if (currentLine.getTag() == Tag.INDI) {
                Individual individual = new Individual(currentLine.getID());
                if (individuals.put(currentLine.getID(), individual) != null) {
                    LOGGER.error(Error.ID_NOT_UNIQUE, individual);
                }
            } else if (currentLine.getTag() == Tag.FAM) {
                Family family = new Family(currentLine.getID());
                if (families.put(currentLine.getID(), family) != null) {
                    LOGGER.error(Error.ID_NOT_UNIQUE, family);
                }
            }
        }
        
        // Populate Individual and Family fields
        for (int i = 0; i < gedLines.size(); i++) {

            Logger.setLineContext(i + 1);

            currentLine = gedLines.get(i);

            if (currentLine.getTag() == Tag.INDI) {
                parseIndividual(gedLines, i, currentLine.getID());
            } else if (currentLine.getTag() == Tag.FAM) {
                parseFamily(gedLines, i, currentLine.getID());
            }
        }
        
        Logger.setLineContext(0);
        s.close();
    }

    private Individual parseIndividual(List<GEDLine> list, int index, String ID) {
        Individual individual = this.individuals.get(ID);
        Tag dateType = null;

        for (int i = index + 1; i < list.size(); i++) {

            Logger.setLineContext(i + 1);

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
                    Family childFamily = this.families.get(gedLine.getArgs());
                    if (childFamily != null) {
                        individual.addChildFamily(childFamily);
                    } else {
                        LOGGER.error(Error.CORRESPONDING_ENTRIES_NOT_FOUND);
                    }
                    break;
                case FAMS:
                    Family spouseFamily = this.families.get(gedLine.getArgs());
                    if (spouseFamily != null) {
                        individual.addSpouseFamily(this.families.get(gedLine.getArgs()));
                    } else {
                        LOGGER.error(Error.CORRESPONDING_ENTRIES_NOT_FOUND);
                    }
                    break;
                default:
                    break;
            }

            if (gedLine.getTag() == Tag.DATE && dateType != null) {
                DateFormat formatter = new SimpleDateFormat(GEDCOM_DATE_FORMAT);
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
            } else {
                // TODO: DATE type not specified
            }

        }
        return individual;
    }

    private Family parseFamily(List<GEDLine> list, int index, String ID) {
        Family family = this.families.get(ID);
        Tag dateType = null;

        for (int i = index + 1; i < list.size(); i++) {

            Logger.setLineContext(i + 1);

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
                    Individual husband = this.individuals.get(gedLine.getArgs());
                    if (husband != null) {
                        family.setHusband(husband);
                    } else {
                        LOGGER.error(Error.CORRESPONDING_ENTRIES_NOT_FOUND);
                    }
                    break;
                case WIFE:
                    Individual wife = this.individuals.get(gedLine.getArgs());
                    if (wife != null) {
                        family.setWife(wife);
                    } else {
                        LOGGER.error(Error.CORRESPONDING_ENTRIES_NOT_FOUND);
                    }
                    break;
                case CHIL:
                    Individual child = this.individuals.get(gedLine.getArgs());
                    if (child != null) {
                        family.addChild(child);
                    } else {
                        LOGGER.error(Error.CORRESPONDING_ENTRIES_NOT_FOUND);
                    }
                    break;
                default:
                    break;
            }

            if (gedLine.getTag() == Tag.DATE && dateType != null) {
                DateFormat formatter = new SimpleDateFormat(GEDCOM_DATE_FORMAT);
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
            } else {
                // TODO: DATE type not specified
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
