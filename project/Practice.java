package project;

import java.io.*;
import java.util.Scanner;

public class Practice {

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
        return String.format("%s|%s|%s|%s", params[0], params[1], (isValidTag(params[1]) ? "Y" : "N"),
                (params.length == 3 ? params[2] : ""));
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
