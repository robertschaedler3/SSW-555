package gedcom.cli;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gedcom.models.Family;
import gedcom.models.GEDFile;
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

    private static void listLargeAgeDiff(GEDFile gedFile) {
        // TODO        
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
