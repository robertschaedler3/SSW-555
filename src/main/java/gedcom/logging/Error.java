package gedcom.logging;

public enum Error {
    ;

    private final ErrorType errorType;
    private final int code;
    private final String message;

    private Error(ErrorType errorType, int code, String message) {
        this.errorType = errorType;
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s US%d: %s", errorType.toString(), code, message);
    }

}
