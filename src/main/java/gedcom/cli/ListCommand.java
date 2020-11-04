package gedcom.cli;

import gedcom.models.GEDFile;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(
    name = "list",
    sortOptions = false, 
    synopsisHeading = "%n", 
    descriptionHeading = "%n@|bold,underline Description|@:%n%n", 
    parameterListHeading = "%n@|bold,underline Parameters|@:%n", 
    optionListHeading = "%n@|bold,underline Options|@:%n", 
    description = "List features and metrics for a given GEDCOM file"
)
public class ListCommand implements Runnable {

    @Mixin
    private FileParameter fileParam;

    @ArgGroup(exclusive = true, multiplicity = "1")
    private ListOptions listOptions = new ListOptions();

    public void run() {
        GEDFile gedFile = fileParam.getFile();
        if (gedFile == null) {
            System.out.println("File not found!");
            return;
        }

        listOptions.list(gedFile);
    }

}
