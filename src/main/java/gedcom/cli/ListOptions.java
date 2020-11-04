package gedcom.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;
import gedcom.models.Table;
import picocli.CommandLine.Option;

public class ListOptions {

    @Option(names = { "-A", "--all" }, description = "List all feature groups")
    protected boolean all;

    @Option(names = { "-dd", "--deceased" }, description = "List all deceased individuals")
    protected boolean listDeceased;

    @Option(names = { "-lm", "--living-married" }, description = "List living married individuals")
    protected boolean listLivingMarried;

    @Option(names = { "-ls", "--living-single" }, description = "List living single individuals")
    protected boolean listLivingSingle;

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

    @Option(names = { "-rs", "--recent-survivors" }, description = "List living descendants/spouses of individuals who died in the last 30 days")
    protected boolean listRecentSurvivors;

    @Option(names = { "-ub", "--upcoming-births" }, description = "List individuals with birthdays in the next 30 days")
    protected boolean listUpcomingBirths;

    @Option(names = { "-ua", "--upcoming-anniversaries" }, description = "List all families with a marriage anniversary in the next 30 days")
    protected boolean listUpcomingAnniversaries;
    
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
    
    private static void listDeceased(GEDFile gedFile) {
        // TODO
    }

    private static void listLivingMarried(GEDFile gedFile) {
        // TODO
    }

    private static void listLivingSingle(GEDFile gedFile) {
        // TODO
    }

    private static void listMultipleBirths(GEDFile gedFile) {
        // TODO
    }

    private static void listOrphans(GEDFile gedFile) {
        // TODO
    }

    private static int diffInYears(Date d1, Date d2) {
        return 0;
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
                int husbandAge = diffInYears(husbandBirth, marriage);
                int wifeAge = diffInYears(wifeBirth, marriage);

                if (Math.max(husbandAge, wifeAge) >= Math.min(husbandAge, wifeAge) * 2) {
                    table.appendRow(family);
                }
            }
        }
        
        System.out.println(table);
    }

    private static void listRecentBirths(GEDFile gedFile) {
        // TODO
    }

    private static void listRecentDeaths(GEDFile gedFile) {
        // TODO
    }

    private static void listRecentSurvivors(GEDFile gedFile) {
        // TODO
    }

    private static void listUpcomingBirths(GEDFile gedFile) {
        // TODO
    }

    private static void listUpcomingAnniversaries(GEDFile gedFile) {
        // TODO
    }

}
