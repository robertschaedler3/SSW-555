package gedcom;

import gedcom.cli.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;


@Command(
    name = "ged",
    subcommands = {
        ValidateCommand.class,
        ListCommand.class,
        CommandLine.HelpCommand.class
    }
)
public class GEDParser {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GEDParser()).execute(args);
        System.exit(exitCode);
    }

}
