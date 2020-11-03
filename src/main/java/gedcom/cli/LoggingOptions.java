package gedcom.cli;

import picocli.CommandLine.Option;

public class LoggingOptions {

    @Option(names = { "-O", "--out" }, description = "Log to a specified file instead of printing to the console")
    protected String logFile;

}
