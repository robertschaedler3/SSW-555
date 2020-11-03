package gedcom.cli;

import gedcom.models.GEDFile;
import gedcom.validators.Validator;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(name = "validate", description = "%nVaildate a given GEDCOM file against a set of validators%n")
public class ValidateCommand implements Runnable {

    @Mixin
    private FileParameter fileParam;

    @ArgGroup(exclusive = true, heading = "%nLogging options%n%n")
    private LoggingOptions loggingOptions = new LoggingOptions();

    @ArgGroup(exclusive = true, multiplicity = "1", heading = "%nValidation options%n%n")
    private ValidateOptions validateOptions = new ValidateOptions();

    public void run() {

        GEDFile gedFile = fileParam.getFile();
        if (gedFile == null) {
            System.out.println("File not found!");
            return;
        }

        Validator validator = validateOptions.buildValidator();

        if (loggingOptions.log) {
            // TODO: set logger output to console
        }

        if (loggingOptions.logFile != null) {
            // TODO: set logger output to file
        }

        boolean valid = validator.isValid(gedFile);

        System.out.printf("\nGEDCOM file status: %s\n", valid ? "VALID" : "INVALID");

    }

}
