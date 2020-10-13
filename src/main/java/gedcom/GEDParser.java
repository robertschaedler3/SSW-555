package gedcom;

import gedcom.models.GEDFile;
import gedcom.validators.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GEDParser {

    private static GEDFile gedFile;
    private static Validator validator = new DefaultValidator();

    private static void parseArgs(String[] args) {
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--siblings-ordered-by-age":
                    validator = new SiblingsOrderedByAge(validator);
                    break;
                case "--correct-gender":
                    validator = new CorrectGender(validator);
                    break;
                case "--multiple-births":
                    validator = new MultipleBirths(validator);
                    break;
                case "--less-than-150-years-old":
                    validator = new LessThan150YearsOld(validator);
                    break;
                case "--divorce-before-death":
                    validator = new DivorceBeforeDeath(validator);
                    break;
                case "--dates-before-current":
                    validator = new DatesBeforeCurrentDate(validator);
                    break;
                case "--no-bigamy":
                    validator = new NoBigamy(validator);
                    break;
                case "--no-incest":
                    validator = new NoIncest(validator);
                    break;
                case "--marriage-before-divorce":
                    validator = new MarriageBeforeDivorce(validator);
                    break;
                case "--marriage-after-14":
                    validator = new MarriageBeforeDivorce(validator);
                    break;
                case "--valid-birth":
                    validator = new ValidBirth(validator);
                    break;
                case "--marriage-before-death":
                    validator = new MarriageBeforeDeath(validator);
                    break;
                case "--parents-not-too-old":
                    validator = new ParentsNotTooOld(validator);
                    break;
                case "--tabulate":
                    System.out.println(gedFile);
                    break;
                default:
                    break;
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: ./GEDParser <.ged file> [ARGS]");
            return;
        }

        File file;
        Scanner scanner;

        try {
            file = new File(args[0]);
            scanner = new Scanner(file);
            gedFile = new GEDFile(scanner);

            parseArgs(args);

            scanner.close();

            boolean valid = validator.isValid(gedFile);
            System.out.println(String.format("gedcom is %s", valid ? "VALID" : "INVALID"));
        } catch (FileNotFoundException fe) {
            System.out.println("The file you entered could not be found.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

}
