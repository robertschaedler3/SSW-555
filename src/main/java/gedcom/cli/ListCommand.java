package gedcom.cli;

import gedcom.models.GEDFile;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.FileNotFoundException;

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

    @CommandLine.Parameters(index = "0", description = "The GEDCOM input file.")
    private String filePath;

    @CommandLine.Parameters(index = "1",  description = "The ancestor who's descendants to view")
    protected String ancestor = "@I1@";

    @ArgGroup(exclusive = true, multiplicity = "1")
    private ListOptions listOptions = new ListOptions();

    protected GEDFile getFile() {
        try {
            File file = new File(filePath);
            return new GEDFile(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public void run() {

        GEDFile gedFile = null;

        if ((gedFile = this.getFile()) == null) {
            System.out.println("File not found!");
            return;
        }

        listOptions.list(gedFile, ancestor);

    }

}
