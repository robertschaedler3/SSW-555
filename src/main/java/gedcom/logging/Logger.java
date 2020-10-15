package gedcom.logging;

public abstract class Logger {

    private static boolean raiseErrors = false;

    public static void raiseAll() {
        raiseErrors = true;
    }

    public static void printAll() {
        raiseErrors = false;
    }

    public static void log(Error error) {
        if (raiseErrors) {
            throw new RuntimeException(error.toString());
        } else {
            System.err.println(error);
        }
    }

}
