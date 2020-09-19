package project;

import java.io.*;
import java.util.Scanner;

public class GEDParser {

    public static boolean isValidTag(String str) {
        try {
            Tag.valueOf(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String parseLine(String line) {
        String[] params = line.split(" ", 3);
        boolean validTag = isValidTag(params[1]);
        String tag = params[1];
        String args = params.length == 3 ? params[2] : "";

        if (!validTag && params.length == 3) { // check if params[1] is an id
            validTag = isValidTag(params[2])
                    && (Tag.valueOf(params[2]) == Tag.FAM || Tag.valueOf(params[2]) == Tag.INDI);
            if (validTag) {
                tag = params[2];
                args = params[1];
            }
        }

        return String.format("%s|%s|%s|%s", params[0], tag, (validTag ? "Y" : "N"), args);
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.err.printf("Usage: <GEDCOM path>\n");
            System.exit(1);
        }

        File text = new File(args[0]);
        Scanner s = new Scanner(text);
        String line;

        while (s.hasNextLine()) {
            line = s.nextLine();
            System.out.printf("--> %s\n", line);

            line = parseLine(line);
            System.out.printf("<-- %s\n", line);
        }

        s.close();
    }

}
