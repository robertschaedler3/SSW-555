package gedcom.builders;

import gedcom.interfaces.Tag;

public class GEDLineBuilder {

    private static String LINE_FORMAT = "%d %s %s\n";

    public static String build(int level, Tag tag) {
        return String.format(LINE_FORMAT, level, tag, " ");
    }

    public static String build(int level, Tag tag, String args) {
        return String.format(LINE_FORMAT, level, tag, args);
    }

    public static String build(int level, String ID, Tag tag) {
        return String.format(LINE_FORMAT, level, ID, tag);
    }

}
