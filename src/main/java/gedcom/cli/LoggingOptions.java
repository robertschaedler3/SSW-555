package gedcom.cli;

import picocli.CommandLine.Option;

public class LoggingOptions {

    @Option(names = { "-o", "--out" }, description = "Log to a specified file instead of printing to the console")
    protected String logfile;

}
