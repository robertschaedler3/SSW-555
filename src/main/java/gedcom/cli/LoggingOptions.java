package gedcom.cli;

import picocli.CommandLine.Option;

public class LoggingOptions {

    @Option(names = { "-L", "--log" }, description = "Log errors to the console.")
    protected boolean log;

    @Option(names = { "-O", "--out" }, description = "Log to a specified file. Override -L,--log")
    protected String logFile;

}
