package gedcom.logging;

import java.util.Arrays;

import gedcom.models.GEDObject;

public class Logger {

    private static String MESSAGE_FORMAT = "%s US%02d %s>> %s %s";
    private static String LINE_FORMAT = "[LINE %d] ";

    private static Logger logger = new Logger();

    private Logger() {
        // Use getInstance()
    }

    public static Logger getInstance() {
        return logger;
    }

    private String line(int line) {
        return String.format(LINE_FORMAT, line);
    }

    private String context(GEDObject... objs) {
        return Arrays.asList(objs).toString();
    }

    public void log(Error error) {
        String fullMessage = String.format(MESSAGE_FORMAT, error.type(), error.code(), "", error, "");
        System.out.println(fullMessage);
    }

    public void log(Error error, GEDObject... objs) {
        String fullMessage = String.format(MESSAGE_FORMAT, error.type(), error.code(), "", error.message(), context(objs));
        System.out.println(fullMessage);
    }

    public void log(int line, Error error) {
        String fullMessage = String.format(MESSAGE_FORMAT, error.type(), error.code(), line(line), error, "");
        System.out.println(fullMessage);
    }

    public void log(int line, Error error, GEDObject... objs) {
        String fullMessage = String.format(MESSAGE_FORMAT, error.type(), error.code(), line(line), error.message(), context(objs));
        System.out.println(fullMessage);
    }

}
