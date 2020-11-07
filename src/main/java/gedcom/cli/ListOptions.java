package gedcom.cli;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import gedcom.models.Table;
import picocli.CommandLine.Option;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import gedcom.interfaces.Gender;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import gedcom.models.Table;
import picocli.CommandLine.Option;

public class ListOptions {

    @Option(names = {"-A", "--all"}, description = "List all feature groups")
    protected boolean all;

    @Option(names = {"-dd", "--deceased"}, description = "List all deceased individuals")
    protected boolean listDeceased;

    @Option(names = {"-lm", "--living-married"}, description = "List living married individuals")
    protected boolean listLivingMarried;

    @Option(names = {"-ls", "--living-single"}, description = "List living single individuals")
    protected boolean listLivingSingle;

    @Option(names = {"-mb", "--mult-births"}, description = "List all multiple births")
    protected boolean listMultipleBirths;

    @Option(names = {"-or", "--orphans"}, description = "List all orphans")
    protected boolean listOrphans;

    @Option(names = {"-ad", "--age-diff"}, description = "List couples with large age differences")
    protected boolean listLargeAgeDiff;

    @Option(names = {"-rb", "--recent-births"}, description = "List all individuals born in the last 30 days")
    protected boolean listRecentBirths;

    @Option(names = {"-rd", "--recent-deaths"}, description = "List all individuals who died in the last 30 days")
    protected boolean listRecentDeaths;

    @Option(names = {"-rs",
            "--recent-survivors"}, description = "List living descendants/spouses of individuals who died in the last 30 days")
    protected boolean listRecentSurvivors;

    @Option(names = {"-ub", "--upcoming-births"}, description = "List individuals with birthdays in the next 30 days")
    protected boolean listUpcomingBirths;

    @Option(names = {"-ua",
            "--upcoming-anniversaries"}, description = "List all families with a marriage anniversary in the next 30 days")
    protected boolean listUpcomingAnniversaries;

    private static void listDeceased(GEDFile gedFile) {
        // TODO
    }

    /**
     * Lists all living individuals who are currently married.
     */
    private static void listLivingMarried(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "NAME"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(Arrays.asList(Individual::getID, Individual::getName));

        Table<Individual> table = new Table<>("Living married", columns, expanders);

        individualSearch:
        for (Individual individual: gedFile.getIndividuals()) {
            if(individual.alive()){
                for(Family family : individual.getSpouseFamilies()) {
                    // Individual is currently in an active, non-divorced marriage
                    if((family.getDivorce() == null) && (family.getHusband().alive() && family.getWife().alive())) {
                        continue;
                    } else { // Individual is currently single
                        continue individualSearch;
                    }
                }
                table.add(individual);
            }
        }

        System.out.println(table);
    }

    /**
     * Lists all living individuals who are currently not married.
     */
    private static void listLivingSingle(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "NAME"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(Arrays.asList(Individual::getID, Individual::getName));

        Table<Individual> table = new Table<>("Living singles", columns, expanders);

        individualSearch:
        for (Individual individual: gedFile.getIndividuals()) {
            if(individual.alive()){
                for(Family family : individual.getSpouseFamilies()) {
                    // Individual is currently in an active, non-divorced marriage
                    if((family.getDivorce() == null) && (family.getHusband().alive() && family.getWife().alive())) {
                        continue individualSearch;
                    }
                }
                table.add(individual);
            }
        }

        System.out.println(table);
    }

    private static void listMultipleBirths(GEDFile gedFile) {
        // TODO
    }

    private static void listOrphans(GEDFile gedFile) {
        // TODO
    }

    private static long daysBetween(Date d1, Date d2) {
        return ChronoUnit.DAYS.between(d1.toInstant(), d2.toInstant());
    }

    private static long yearsBetween(Date d1, Date d2) {
        return ChronoUnit.YEARS.between(d1.toInstant(), d2.toInstant());
    }

    /**
     * List all couples who were married when the older spouse was more than twice
     * as old as the younger spouse.
     */
    private static void listLargeAgeDiff(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "HUSBAND", "WIFE", "MARRIAGE"));
        List<Function<? super Family, ? extends Object>> expanders = new ArrayList<>(Arrays.asList(Family::getID, Family::getHusband, Family::getWife, Family::getMarriage));

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
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(Arrays.asList(Individual::getID, Individual::getBirthday));

        Table<Individual> table = new Table<>("Recent birthdays", columns, expanders);

        for (Individual ind : gedFile.getIndividuals()) {
            Date now = new Date();
            Date indBirthday = ind.getBirthday();
            if (indBirthday == null) continue;

            if (daysBetween(indBirthday, now) < 30) {
                table.add(ind);
            }
        }

        System.out.println(table);
    }

    private static void listRecentDeaths(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "DEATH"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(Arrays.asList(Individual::getID, Individual::getDeath));

        Table<Individual> table = new Table<>("Recent deaths", columns, expanders);

        for (Individual ind : gedFile.getIndividuals()) {
            Date now = new Date();
            Date indDeath = ind.getDeath();
            if (indDeath == null) continue;

            if (daysBetween(indDeath, now) < 30) {
                table.add(ind);
            }
        }

        System.out.println(table);
    }

    private static void listRecentSurvivors(GEDFile gedFile) {
        List<String> columns = new ArrayList<>(Arrays.asList("ID", "BIRTH", "AGE"));
        List<Function<? super Individual, ? extends Object>> expanders = new ArrayList<>(Arrays.asList(Individual::getID, Individual::getBirthday, Individual::age));

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

    private static void listUpcomingBirths(GEDFile gedFile) {
        // TODO
    }

    private static void listUpcomingAnniversaries(GEDFile gedFile) {
        // TODO
    }

    private boolean listSelected(boolean list) {
        return all || list;
    }

    protected void list(GEDFile gedFile) {

        if (listSelected(listDeceased)) {
            listDeceased(gedFile);
        }

        if (listSelected(listLivingMarried)) {
            listLivingMarried(gedFile);
        }

        if (listSelected(listLivingSingle)) {
            listLivingSingle(gedFile);
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

        if (listSelected(listRecentSurvivors)) {
            listRecentSurvivors(gedFile);
        }

        if (listSelected(listUpcomingBirths)) {
            listUpcomingBirths(gedFile);
        }

        if (listSelected(listUpcomingAnniversaries)) {
            listUpcomingAnniversaries(gedFile);
        }

    }

}
