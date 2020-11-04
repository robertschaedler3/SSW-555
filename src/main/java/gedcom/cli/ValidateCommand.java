package gedcom.cli;

import gedcom.logging.Logger;
import gedcom.models.GEDFile;
import gedcom.validators.Validator;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(
    name = "validate",
    sortOptions = false, 
    synopsisHeading = "%n", 
    descriptionHeading = "%n@|bold,underline Description|@:%n%n", 
    parameterListHeading = "%n@|bold,underline Parameters|@:%n", 
    optionListHeading = "%n@|bold,underline Options|@:%n", 
    header = "Validate a GEDCOM file.",
    description = "Vaildate a GEDCOM file against a set of validators."
)
public class ValidateCommand implements Runnable {

    @Mixin
    private FileParameter fileParam;

    @Mixin
    private LoggingOptions loggingOptions = new LoggingOptions();

    @ArgGroup(exclusive = false, multiplicity = "1")
    private ValidateOptions validateOptions = new ValidateOptions();

    public void run() {

        GEDFile gedFile = fileParam.getFile();
        if (gedFile == null) {
            System.out.println("File not found!");
            return;
        }

        Validator validator = validateOptions.buildValidator();

        if (loggingOptions.logfile != null) {
            Logger.setOutput(loggingOptions.logfile);
        }

        boolean valid = validator.isValid(gedFile);

        System.out.printf("\nGEDCOM file status: %s\n", valid ? "VALID" : "INVALID");

    }

}
