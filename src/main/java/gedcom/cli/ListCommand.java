package gedcom.cli;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(name = "list", description = "%nList features and metrics for a given GEDCOM file%n")
public class ListCommand implements Runnable {

    @Mixin
    private FileParameter fileParam;

    @ArgGroup(exclusive = true, multiplicity = "1", heading = "%nList options%n%n")
    private ListOptions listOptions = new ListOptions();

    public void run() {
        listOptions.list();
    }

}
