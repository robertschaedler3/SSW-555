package gedcom.logging;

import java.util.Arrays;

import gedcom.models.GEDObject;

public class Logger {

    private static String MESSAGE_FORMAT = "%s US%02d %s>> %s %s";
    private static String LINE_FORMAT = "[LINE %d] ";
    
    private static String ERROR = "ERROR";
    private static String ANOMALY = "ANOMALY";

    private static int line = 0;

    private static Logger logger = new Logger();

    private Logger() { // Use getInstance()
    }

    public static Logger getInstance() {
        return logger;
    }

    public void setLineContext(int n) {
        line = n;
    }

    private String line() {
        return (line > 0) ? String.format(LINE_FORMAT, line) : "";
    }

    private String context(GEDObject... objs) {
        return Arrays.asList(objs).toString();
    }

    private void log(String type, int code, String message, String context) {
        String fullMessage = String.format(MESSAGE_FORMAT, type, code, line(), message.toUpperCase(), context);
        System.out.println(fullMessage);
    }

    public void error(Error error) {
        log(ERROR, error.code(), error.message(), "");
    }

    public void error(Error error, String message) {
        log(ERROR, error.code(), message, "");
    }

    public void error(Error error, GEDObject... objs) {
        log(ERROR, error.code(), error.message(), context(objs));
    }

    public void anomaly(Error anomaly) {
        log(ANOMALY, anomaly.code(), anomaly.message(), "");
    }

    public void anomaly(Error anomaly, String message) {
        log(ANOMALY, anomaly.code(), message, "");
    }

    public void anomaly(Error anomaly, GEDObject... objs) {
        log(ANOMALY, anomaly.code(), anomaly.message(), context(objs));
    }

}
