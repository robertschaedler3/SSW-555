package gedcom.models;

import gedcom.interfaces.Tag;

public class GEDLine {

    private int level;
    private Tag tag;
    private String args;
    private String ID;

    public GEDLine(String line) {
        String[] params = line.split(" ", 3);

        level = Integer.parseInt(params[0]);

        if (level == 0) {
            if (parseTag(params[1]) != Tag.INVALID) { // <level> <tag> <ignorable args>
                tag = parseTag(params[1]);
                args = (params.length == 3) ? params[2] : "";
            } else if (params.length == 3 && parseTag(params[2]) != Tag.INVALID) { // <level> <id> <tag>
                tag = parseTag(params[2]);
                ID = params[1];
            }
        } else {
            tag = parseTag(params[1]);
            args = (params.length == 3) ? params[2] : "";
        }
    }

    public int getLevel() {
        return level;
    }

    public Tag getTag() {
        return tag;
    }

    public String getArgs() {
        return args;
    }

    public String getID() {
        return ID;
    }

    private Tag parseTag(String str) {
        try {
            return Tag.valueOf(str);
        } catch (IllegalArgumentException e) {
            return Tag.INVALID;
        }
    }

}
