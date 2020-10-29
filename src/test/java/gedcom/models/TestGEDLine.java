package gedcom.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import gedcom.interfaces.Tag;

public class TestGEDLine {

    private String LINE_FORMAT = "%d %s %s";

    private GEDLine createGEDLine(int level, Object o1, Object o2) {
        return new GEDLine(String.format(LINE_FORMAT, level, o1.toString(), o2.toString()));
    }

    @Test
    public void testLevelTagIgnoreableArgs() {
        int level = 0;
        Tag tag = Tag.HEAD;
        String args = "TEST ARGS";

        GEDLine gedLine = createGEDLine(level, tag, args);

        assertEquals(level, gedLine.getLevel());
        assertEquals(tag, gedLine.getTag());
        assertEquals(args, gedLine.getArgs());
    }

    @Test
    public void testLevelTagArgs() {
        int level = 1;
        Tag tag = Tag.DATE;
        String args = "TEST ARGS";

        GEDLine gedLine = createGEDLine(level, tag, args);

        assertEquals(level, gedLine.getLevel());
        assertEquals(tag, gedLine.getTag());
        assertEquals(args, gedLine.getArgs());
    }

    @Test
    public void testLevelIdTag() {
        int level = 0;
        String id = "@I1@";
        Tag tag = Tag.INDI;

        GEDLine gedLine = createGEDLine(level, id, tag);

        assertEquals(level, gedLine.getLevel());
        assertEquals(id, gedLine.getID());
        assertEquals(tag, gedLine.getTag());
    }

}
