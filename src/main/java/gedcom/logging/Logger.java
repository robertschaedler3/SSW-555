package gedcom.logging;

public class Logger {

    private static String MESSAGE_FORMAT = "[LINE %d] %s US%02d >> %s";

    public static void log(int line, Error error) {
        String fullMessage = String.format(MESSAGE_FORMAT, line, error.type(), error.code(), error);
        System.out.println(fullMessage);
    }

    public static void log(int line, Error error, String msgFmt, Object... args) {
        String messageContext = String.format(MESSAGE_FORMAT, line, error.type(), error.code(), msgFmt);
        String fullMessage = String.format(messageContext, args);
        System.out.println(fullMessage);
    }

}
