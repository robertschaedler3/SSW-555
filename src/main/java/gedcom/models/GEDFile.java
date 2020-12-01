package gedcom.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    private static final int TOSTRING_LIST_LENGTH = 75;
    public static final String FULL_DATE_FORMAT = "dd MMM yyyy";
    public static final String[] GEDCOM_DATE_FORMATS = { FULL_DATE_FORMAT, "MMM yyyy", "yyyy" };

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
                Date date = parseDate(gedLine.getArgs(), individual);
                if (date == null) {
                    LOGGER.error(Error.ILLEGITIMATE_DATE, individual);
                } else if (dateType == Tag.BIRT) {
                    individual.setBirthday(date);
                } else if (dateType == Tag.DEAT) {
                    individual.setDeath(date);
                }
                dateType = null;
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
                Date date = parseDate(gedLine.getArgs(), family);
                if (date == null) {
                    LOGGER.error(Error.ILLEGITIMATE_DATE, family);
                } else if (dateType == Tag.MARR) {
                    family.setMarriage(date);
                } else if (dateType == Tag.DIV) {
                    family.setDivorce(date);
                }
                dateType = null;
            }
        }

        return family;
    }

    private Date parseDate(String date, GEDObject gedObject) {
        for (String format : GEDCOM_DATE_FORMATS) {
            try {
                SimpleDateFormat dateFmt = new SimpleDateFormat(format);
                return dateFmt.parse(date);
            } catch (ParseException e) {
                LOGGER.error(Error.PARTIAL_DATE, gedObject);
            }
        }
        return null;
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private List<Individual> individuals;
        private List<Family> families;

        private Builder() {
            this.individuals = new ArrayList<>();
            this.families = new ArrayList<>();
        }

        public Builder individual(Individual individual) {
            this.individuals.add(individual);
            return this;
        }

        public Builder individuals(List<Individual> individuals) {
            this.individuals.addAll(individuals);
            return this;
        }

        public Builder individuals(Individual... individuals) {
            this.individuals.addAll(Arrays.asList(individuals));
            return this;
        }

        public Builder family(Family family) {
            this.families.add(family);
            return this;
        }

        public Builder families(List<Family> families) {
            this.families.addAll(families);
            return this;
        }

        public Builder families(Family... families) {
            this.families.addAll(Arrays.asList(families));
            return this;
        }

        public GEDFile build() {
            return new GEDFile(individuals, families);
        }

    }

    public static Writer writer() {
        return new Writer();
    }

    public static final class Writer {

        private StringBuilder sb;
        private static String LINE_FORMAT = "%d %s %s\n";

        public Writer() {
            this.sb = new StringBuilder();
            sb.append(this.getHead());
        }

        private String getHead() {
            StringBuilder head = new StringBuilder();
            head.append(line(0, Tag.NOTE, "GEDCOM BUILDER"));
            head.append(line(0, Tag.HEAD));
            return head.toString();
        }

        private String getTail() {
            return line(0, Tag.TRLR);
        }

        private static String line(int level, Tag tag) {
            return String.format(LINE_FORMAT, level, tag, " ");
        }

        private static String line(int level, Tag tag, String args) {
            return String.format(LINE_FORMAT, level, tag, args);
        }

        private static String line(int level, String ID, Tag tag) {
            return String.format(LINE_FORMAT, level, ID, tag);
        }

        private void addDateLine(Date date) {
            SimpleDateFormat dateFmt = new SimpleDateFormat(GEDFile.FULL_DATE_FORMAT);
            sb.append(line(2, Tag.DATE, dateFmt.format(date)));
        }

        public void addIndividual(Individual individual) {
            sb.append(line(0, individual.getID(), Tag.INDI));

            if (individual.getName() != null) {
                sb.append(line(1, Tag.NAME, individual.getName()));
            }

            if (individual.getGender() != Gender.NOT_SPECIFIED) {
                sb.append(line(1, Tag.SEX, individual.getGender().toString()));
            }

            if (individual.getBirthday() != null) {
                sb.append(line(1, Tag.BIRT));
                addDateLine(individual.getBirthday());
            }

            if (individual.getDeath() != null) {
                sb.append(line(1, Tag.DEAT));
                addDateLine(individual.getDeath());
            }

            for (Family famc : individual.getChildFamilies()) {
                sb.append(line(1, Tag.FAMC, famc.getID()));
            }

            for (Family fams : individual.getSpouseFamilies()) {
                sb.append(line(1, Tag.FAMS, fams.getID()));
            }
        }

        public Writer individual(Individual individual) {
            addIndividual(individual);
            return this;
        }

        public Writer individuals(Individual... individuals) {
            for (Individual individual : individuals) {
                addIndividual(individual);
            }
            return this;
        }

        public Writer individuals(Iterable<Individual> individuals) {
            for (Individual individual : individuals) {
                addIndividual(individual);
            }
            return this;
        }

        public void addFamily(Family family) {
            sb.append(line(0, family.getID(), Tag.FAM));

            if (family.getHusband() != null) {
                sb.append(line(1, Tag.HUSB, family.getHusband().getID()));
            }

            if (family.getWife() != null) {
                sb.append(line(1, Tag.WIFE, family.getWife().getID()));
            }

            if (family.getMarriage() != null) {
                sb.append(line(1, Tag.MARR));
                addDateLine(family.getMarriage());
            }

            if (family.getDivorce() != null) {
                sb.append(line(1, Tag.DIV));
                addDateLine(family.getDivorce());
            }

            for (Individual child : family.getChildren()) {
                sb.append(line(1, Tag.CHIL, child.getID()));
            }
        }

        public Writer family(Family family) {
            addFamily(family);
            return this;
        }

        public Writer families(Family... families) {
            for (Family family : families) {
                addFamily(family);
            }
            return this;
        }

        public Writer families(Iterable<Family> families) {
            for (Family family : families) {
                addFamily(family);
            }
            return this;
        }

        public String build() {
            sb.append(this.getTail());
            return sb.toString();
        }

        public static File getTempFile(String GEDCOM) {
            try {
                Path tempFile = Files.createTempFile(null, null);
                Files.write(tempFile, GEDCOM.getBytes(StandardCharsets.UTF_8));
                return tempFile.toFile();
            } catch (IOException e) {
                return null;
            }
        }

    }
}
