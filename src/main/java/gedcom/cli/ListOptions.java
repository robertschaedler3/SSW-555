package gedcom.cli;

import picocli.CommandLine.ArgGroup;
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
    
    protected void list() {

        if (listSelected(listDeceased)) {
            listDeceased();
        }

        if (listSelected(listLivingMarried)) {
            listLivingMarried();
        }

        if (listSelected(listLivingSingle)) {
            listLivingSingle();
        }

        if (listSelected(listMultipleBirths)) {
            listMultipleBirths();
        }

        if (listSelected(listOrphans)) {
            listOrphans();
        }

        if (listSelected(listLargeAgeDiff)) {
            listLargeAgeDiff();
        }

        if (listSelected(listRecentBirths)) {
            listRecentBirths();
        }

        if (listSelected(listRecentDeaths)) {
            listRecentDeaths();
        }

        if (listSelected(listRecentSurvivors)) {
            listRecentSurvivors();
        }

        if (listSelected(listUpcomingBirths)) {
            listUpcomingBirths();
        }

        if (listSelected(listUpcomingAnniversaries)) {
            listUpcomingAnniversaries();
        }

    }
    
    private static void listDeceased() {
        // TODO
    }

    private static void listLivingMarried() {
        // TODO
    }

    private static void listLivingSingle() {
        // TODO
    }

    private static void listMultipleBirths() {
        // TODO
    }

    private static void listOrphans() {
        // TODO
    }

    private static void listLargeAgeDiff() {
        // TODO
    }

    private static void listRecentBirths() {
        // TODO
    }

    private static void listRecentDeaths() {
        // TODO
    }

    private static void listRecentSurvivors() {
        // TODO
    }

    private static void listUpcomingBirths() {
        // TODO
    }

    private static void listUpcomingAnniversaries() {
        // TODO
    }

}
