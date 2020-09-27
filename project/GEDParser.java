package project;

import java.io.File;

import project.validators.*;
import project.models.GEDFile;

public class GEDParser {

    public static void main(String[] args) {
        File file = new File(args[0]);

        GEDFile gedFile = new GEDFile(file);

        Validator validator = new DefaultValidator();

        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--no-bigamy":
                    validator = new NoBigamy(validator);
                    break;
                case "--marriage-before-death":
                    validator = new MarriageBeforeDeath(validator);
                    break;
                case "--tablulate":
                    System.out.println(gedFile);
                    break;
                default:
                    break;
            }
        }

        boolean valid = validator.isValid(gedFile);
        System.out.println(String.format("GEDCOM is %s", valid ? "VALID" : "INVALID"));
    }

}
