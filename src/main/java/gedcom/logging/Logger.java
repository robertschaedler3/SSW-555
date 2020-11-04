package gedcom.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gedcom.models.GEDObject;

public class Logger {

    private static String MESSAGE_FORMAT = "%s US%02d [%s] >> %s %s";
    private static String LINE_FORMAT = "LINE %d";

    private static int line = 0;

    private static String file;

    private static Logger logger = new Logger();
    private static List<LogAppender> appenders = new ArrayList<>();

    private Logger() { // Use getInstance()
    }

    public class LogEvent {

        private Level level;
        private int code;
        private int line;
        private String message;
        private GEDObject[] context;

        public LogEvent(Level level, int code, int line, String message, GEDObject... context) {
            this.level = level;
            this.code = code;
            this.line = line;
            this.message = message;
            this.context = context;
        }

        private String line() {
            return (line > 0) ? String.format(LINE_FORMAT, line) : "";
        }

        private String context(GEDObject... objs) {
            return Arrays.asList(objs).toString();
        }

        public Level getLevel() {
            return level;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return String.format(MESSAGE_FORMAT, level, code, line(), message.toUpperCase(), context(context));
        }

        @Override
        public String toString() {
            return getMessage();
        }

    }

    public static Logger getInstance() {
        return logger;
    }

    public static void setLineContext(int n) {
        line = n;
    }

    public static void setOutput(String output) {
        file = output;
    }

    private static void appendToFile(String message) {
        try {
            Writer output = new BufferedWriter(new FileWriter(file, true));
            output.append(message);
            output.append("\n");
            output.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAppender(LogAppender appender) {
        appenders.add(appender);
    }

    private void log(Level level, int code, String message, GEDObject... context) {
        LogEvent logEvent = new LogEvent(level, code, line, message.toUpperCase(), context);
        for (LogAppender appender : appenders) {
            appender.append(logEvent);
        }

        if (file != null) {
            appendToFile(logEvent.toString());
        } else {
            System.out.println(logEvent);
        }
    }

    public void error(Error error) {
        log(Level.ERROR, error.code(), error.message());
    }

    public void error(Error error, String message) {
        log(Level.ERROR, error.code(), message);
    }

    public void error(Error error, GEDObject... objs) {
        log(Level.ERROR, error.code(), error.message(), objs);
    }
    
    public void error(Error error, String message, GEDObject... objs) {
        log(Level.ERROR, error.code(), message, objs);
    }

    public void anomaly(Error anomaly) {
        log(Level.ANOMALY, anomaly.code(), anomaly.message());
    }

    public void anomaly(Error anomaly, String message) {
        log(Level.ANOMALY, anomaly.code(), message);
    }

    public void anomaly(Error anomaly, GEDObject... objs) {
        log(Level.ANOMALY, anomaly.code(), anomaly.message(), objs);
    }
    
    public void anomaly(Error anomaly, String message, GEDObject... objs) {
        log(Level.ANOMALY, anomaly.code(), message, objs);
    }

}
