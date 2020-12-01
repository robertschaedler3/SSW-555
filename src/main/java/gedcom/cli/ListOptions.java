package gedcom.cli;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import gedcom.models.Table;
import picocli.CommandLine.Option;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public class ListOptions {

    @Option(names = { "-A", "--all" }, description = "List all feature groups")
    protected boolean all;

    @Option(names = {"-bm", "--birth-month"}, description = "List all births this month")
    protected boolean listBirthsThisMonth;

    @Option(names = {"-dd", "--deceased"}, description = "List all deceased individuals")
    protected boolean listDeceased;

    @Option(names = { "-lm", "--living-married" }, description = "List living married individuals")
    protected boolean listLivingMarried;

    @Option(names = { "-ma", "--marriages" }, description = "List living married individuals")
    protected boolean listMarriages;

    @Option(names = { "-ma", "--marriages" }, description = "List living married individuals")
    protected boolean listDivorces;

    @Option(names = { "-ls", "--living-single" }, description = "List living single individuals")
    protected boolean listLivingSingle;

    @Option(names = { "-ld", "--list-descendants" }, description = "List all descendants of an individual")
    protected boolean listDescendants;

    @Option(names = { "-mb", "--mult-births" }, description = "List all multiple births")
    protected boolean listMultipleBirths;

    @Option(names = { "-or", "--orphans" }, description = "List all orphans")
    protected boolean listOrphans;

    @Option(names = { "-ad", "--age-diff" }, description = "List couples with large age differences")
    protected boolean listLargeAgeDiff;

    @Option(names = { "-rb", "--recent-births" }, description = "List all individuals born in the last 30 days")
    protected boolean listRecentBirths;

    @Option(names = { "-rd", "--recent-deaths" }, description = "List all individuals who died in the last 30 days")
    protected boolean listRecentDeaths;

    @Option(names = { "-ge", "--generation" }, description = "List individuals with their generation number")
    protected boolean listGenerations;

    @Option(names = { "-gn", "--generation-number" }, description = "List individuals in given generation number e.g. -gn=2")
    protected int listGenerationNumber = -1;

    @Option(names = { "-rs", "--recent-survivors" }, description = "List living descendants/spouses of individuals who died in the last 30 days")
    protected boolean listRecentSurvivors;

    @Option(names = { "-ub", "--upcoming-births" }, description = "List individuals with birthdays in the next 30 days")
    protected boolean listUpcomingBirths;

    @Option(names = { "-ud", "--upcoming-deaths" }, description = "List individuals with death anniversaries in the next 30 days")
    protected boolean listUpComingDeaths;

    @Option(names = { "-ua", "--upcoming-anniversaries" }, description = "List all families with a marriage anniversary in the next 30 days")
    protected boolean listUpcomingAnniversaries;

    private static void listDeceased(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "NAME"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getName));
        Table<Individual> table = new Table<>("Deceased People", columns, expanders);
        for (Individual individual : gedFile.getIndividuals()) {
            if (!individual.alive()) {
                table.add(individual);
            }
        }
        System.out.println(table);
    }

    /**
     * Lists all living individuals who are currently married.
     */
    private static void listLivingMarried(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "NAME"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getName));

        Table<Individual> table = new Table<>("Living married", columns, expanders);

        boolean married = false;

        individualSearch: for (Individual individual : gedFile.getIndividuals()) {
            if (individual.alive()) {
                for (Family family : individual.getSpouseFamilies()) {
                    // Individual is currently in an active, non-divorced marriage
                    if ((family.getDivorce() == null) && (family.getHusband().alive() && family.getWife().alive())) {
                        married = true;
                        continue;
                    } else { // Individual is currently single
                        continue individualSearch;
                    }
                }
                if (married) {
                    table.add(individual);
                    married = false;
                }
            }
        }

        System.out.println(table);
    }

    /**
     * Lists all current marriages.
     */
    private static void listMarriages(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "Husband", "Wife", "Marriage"));
        List<Function<? super Family, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Family::getID, Family::getHusband, Family::getWife, Family::getMarriage));

        Table<Family> table = new Table<>("All Marriages", columns, expanders);

        for (Family family : gedFile.getFamilies()) {
            if (family.getMarriage() != null && family.getHusband() != null && family.getWife() != null) {
                table.add(family);
            }
        }

        System.out.println(table);
    }

    /**
     * Lists all living individuals who are currently not married.
     */
    private static void listLivingSingle(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "NAME"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getName));

        Table<Individual> table = new Table<>("Living singles", columns, expanders);

        individualSearch: for (Individual individual : gedFile.getIndividuals()) {
            if (individual.alive()) {
                for (Family family : individual.getSpouseFamilies()) {
                    // Individual is currently in an active, non-divorced marriage
                    if ((family.getDivorce() == null) && (family.getHusband().alive() && family.getWife().alive())) {
                        continue individualSearch;
                    }
                }
                table.add(individual);
            }
        }

        System.out.println(table);
    }

    /**
     * Lists the descendants of an individual. If no individual is specified, @I1@
     * will always be listed.
     */
    private static void listDescendants(GEDFile gedFile, String ancestor) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "NAME"));

        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getName));
        Table<Individual> table = new Table<>("Descendants", columns, expanders);


        for (Individual individual : gedFile.getIndividuals()) {
            if (individual.getID().equals(ancestor)) {
                for (Individual desc : individual.getDescendants()) {
                    table.add(desc);
                }
                break;
            }
        }

        System.out.println(table);
    }

    /**
     * Lists all current divorces.
     */
    private static void listDivorces(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "Husband", "Wife", "Divorce"));
        List<Function<? super Family, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Family::getID, Family::getHusband, Family::getWife, Family::getDivorce));

        Table<Family> table = new Table<>("All Divorces", columns, expanders);

        for (Family family : gedFile.getFamilies()) {
            if (family.getDivorce() != null && family.getHusband() != null && family.getWife() != null) {
                table.add(family);
            }
        }

        System.out.println(table);
    }

    private static void listMultipleBirths(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "NAME", "BIRTH"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getName, Individual::getBirthday));

        Table<Individual> table = new Table<>("Multiple Birthdays", columns, expanders);

        Map<String, List<Individual>> birthsMap = new HashMap<>();

        for (Individual ind : gedFile.getIndividuals()) {
            if (birthsMap.containsKey(ind.getBirthday().toString())) {
                birthsMap.get(ind.getBirthday().toString()).add(ind);
            } else {
                List<Individual> birthsOnDate = new ArrayList<>();
                birthsOnDate.add(ind);
                birthsMap.put(ind.getBirthday().toString(), birthsOnDate);
            }
        }

        for (Entry<String, List<Individual>> entry : birthsMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                for (Individual indi : entry.getValue()) {
                    table.add(indi);
                }
            }
        }

        System.out.println(table);
    }

    private static void listOrphans(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "NAME"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getName));
        Table<Individual> table = new Table<>("Orphans", columns, expanders);
        for (Individual individual : gedFile.getIndividuals()) {
            if (individual.alive()) {
                List<Individual> parents = individual.getParents();
                if ((!parents.isEmpty()) && (parents.get(0).alive() || parents.get(1).alive())) { // if either are alive
                    continue;
                } else {
                    table.add(individual);
                }
            }
        }
        System.out.println(table);
    }

    private static long daysBetween(Date d1, Date d2) {
        return ChronoUnit.DAYS.between(d1.toInstant(), d2.toInstant());
    }

    private static long yearsBetween(Date d1, Date d2) {
        return (d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24 * 365);
    }

    /**
     * List all couples who were married when the older spouse was more than twice
     * as old as the younger spouse.
     */
    private static void listLargeAgeDiff(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "HUSBAND", "WIFE", "MARRIAGE"));
        List<Function<? super Family, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Family::getID, Family::getHusband, Family::getWife, Family::getMarriage));

        Table<Family> table = new Table<>("Large age difference", columns, expanders);

        for (Family family : gedFile.getFamilies()) {
            Individual husband = family.getHusband();
            Individual wife = family.getHusband();

            Date marriage = family.getMarriage();
            Date husbandBirth = husband != null ? husband.getBirthday() : null;
            Date wifeBirth = wife != null ? wife.getBirthday() : null;

            if (marriage != null && husbandBirth != null && wifeBirth != null) {
                long husbandAge = yearsBetween(husbandBirth, marriage);
                long wifeAge = yearsBetween(wifeBirth, marriage);

                if (Math.max(husbandAge, wifeAge) >= Math.min(husbandAge, wifeAge) * 2) {
                    table.add(family);
                }
            }
        }

        System.out.println(table);
    }

    private static void listRecentBirths(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "BIRTH"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getBirthday));

        Table<Individual> table = new Table<>("Recent birthdays", columns, expanders);

        for (Individual ind : gedFile.getIndividuals()) {
            Date now = new Date();
            Date indBirthday = ind.getBirthday();
            if (indBirthday == null)
                continue;

            if (daysBetween(indBirthday, now) < 30) {
                table.add(ind);
            }
        }

        System.out.println(table);
    }

    private static void listRecentDeaths(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "DEATH"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getDeath));

        Table<Individual> table = new Table<>("Recent deaths", columns, expanders);

        for (Individual ind : gedFile.getIndividuals()) {
            Date now = new Date();
            Date indDeath = ind.getDeath();
            if (indDeath == null)
                continue;

            if (daysBetween(indDeath, now) < 30) {
                table.add(ind);
            }
        }

        System.out.println(table);
    }

    private static void listGenerations(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "NAME", "GENERATION"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getName, Individual::getGeneration));

        Table<Individual> table = new Table<>("Individual generations", columns, expanders);

        for (Individual indi : gedFile.getIndividuals()) {
            table.add(indi);
        }

        System.out.println(table);
    }

    private static void listGeneration(GEDFile gedFile, int genNumber) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "NAME", "GENERATION"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getName, Individual::getGeneration));

        Table<Individual> table = new Table<>("Individual generations", columns, expanders);

        for (Individual indi : gedFile.getIndividuals()) {
            if (indi.getGeneration() == genNumber)
                table.add(indi);
        }

        System.out.println(table);
    }

    private static void listRecentSurvivors(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "BIRTH", "AGE"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getBirthday, Individual::age));

        Table<Individual> table = new Table<>("Recent survivors", columns, expanders);

        for (Family family : gedFile.getFamilies()) {

            Individual husband = family.getHusband();
            Individual wife = family.getWife();

            Date husbandDeath = husband != null ? husband.getDeath() : null;
            Date wifeDeath = wife != null ? wife.getDeath() : null;

            if (husbandDeath != null && daysBetween(husbandDeath, new Date()) < 30) {
                if (wife != null) {
                    table.add(wife);
                }
                table.addAll(family.getChildren());
            } else if (wifeDeath != null && daysBetween(wifeDeath, new Date()) < 30) {
                if (husband != null) {
                    table.add(husband);
                }
                table.addAll(family.getChildren());
            }
        }

        System.out.println(table);
    }

    private static Date dateThisYear(Date date) {
        if (date == null) {
            throw new IllegalArgumentException();
        }

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        cal.setTime(date);
        cal.set(Calendar.YEAR, year);

        return cal.getTime();
    }

    private static void listUpcomingBirths(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "BIRTH"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getBirthday));

        Table<Individual> table = new Table<>("Upcoming birthdays", columns, expanders);

        for (Individual indi : gedFile.getIndividuals()) {
            Date now = new Date();
            Date birth = indi.getBirthday();
            if (birth == null)
                continue;
            Date birthdayThisYear = dateThisYear(birth);

            if (daysBetween(birthdayThisYear, now) < 30) {
                table.add(indi);
            }
        }

        System.out.println(table);
    }

    /**
     * Lists all individuals who's anniversaries of death fall within 30 days
     */
    private static void listUpComingDeaths(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "DEATH"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Individual::getID, Individual::getDeath));

        Table<Individual> table = new Table<>("Upcoming death anniversaries", columns, expanders);

        for (Individual indi : gedFile.getIndividuals()) {
            Date now = new Date();
            Date death = indi.getDeath();
            if (death == null)
                continue;
            Date deathDayThisYear = dateThisYear(death);

            if (daysBetween(deathDayThisYear, now) < 30) {
                table.add(indi);
            }
        }

        System.out.println(table);
    }

    private static void listUpcomingAnniversaries(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "HUSBAND", "WIFE", "MARRIAGE"));
        List<Function<? super Family, ? extends Object>> expanders = new ArrayList<>(
                Arrays.asList(Family::getID, Family::getHusband, Family::getWife, Family::getMarriage));

        Table<Family> table = new Table<>("Upcoming Anniversaries", columns, expanders);

        for (Family family : gedFile.getFamilies()) {
            Date now = new Date();
            Date marriage = family.getMarriage();
            if (marriage == null)
                continue;
            Date anniversary = dateThisYear(marriage);

            if (daysBetween(anniversary, now) < 30) {
                table.add(family);
            }
        }

        System.out.println(table);
    }

    private static void listBirthsThisMonth(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "BIRTH"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(Arrays.asList(Individual::getID, Individual::getBirthday));

        Table<Individual> table = new Table<>("Birthdays this month", columns, expanders);

        for (Individual indi : gedFile.getIndividuals()) {
            Date now = new Date();
            Date birth = indi.getBirthday();
            if (birth == null) continue;

            if (now.getMonth() == birth.getMonth()) {
                table.add(indi);
            }
        }

        System.out.println(table);
    }

    private boolean listSelected(boolean list) {
        return all || list;
    }

    protected void list(GEDFile gedFile, String ancestor) {

        if (listSelected(listDeceased)) {
            listDeceased(gedFile);
        }

        if (listSelected(listLivingMarried)) {
            listLivingMarried(gedFile);
        }

        if (listSelected(listMarriages)) {
            listMarriages(gedFile);
        }

        if (listSelected(listDivorces)) {
            listDivorces(gedFile);
        }

        if (listSelected(listLivingSingle)) {
            listLivingSingle(gedFile);
        }

        if (listSelected(listDescendants)) {
            listDescendants(gedFile, ancestor);
        }

        if (listSelected(listMultipleBirths)) {
            listMultipleBirths(gedFile);
        }

        if (listSelected(listOrphans)) {
            listOrphans(gedFile);
        }

        if (listSelected(listLargeAgeDiff)) {
            listLargeAgeDiff(gedFile);
        }

        if (listSelected(listRecentBirths)) {
            listRecentBirths(gedFile);
        }

        if (listSelected(listRecentDeaths)) {
            listRecentDeaths(gedFile);
        }

        if (listSelected(listGenerations)) {
            listGenerations(gedFile);
        }

        if (listGenerationNumber != -1) {
            listGeneration(gedFile, listGenerationNumber);
        }

        if (listSelected(listRecentSurvivors)) {
            listRecentSurvivors(gedFile);
        }

        if (listSelected(listUpcomingBirths)) {
            listUpcomingBirths(gedFile);
        }

        if (listSelected(listUpComingDeaths)) {
            listUpComingDeaths(gedFile);
        }

        if (listSelected(listUpcomingAnniversaries)) {
            listUpcomingAnniversaries(gedFile);
        }

        if (listSelected(listBirthsThisMonth)) {
            listBirthsThisMonth(gedFile);
        }
        
    }

}
