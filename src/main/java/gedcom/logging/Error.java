package gedcom.logging;

public enum Error {
    DATE_BEFORE_CURRENT_DATE(1),
    BIRTH_BEFORE_MARRIAGE(2),
    BIRTH_BEFORE_DEATH(3),
    MARRIAGE_BEFORE_DIVORCE(4),
    MARRIAGE_BEFORE_DEATH(5),
    DIVORCE_BEFORE_DEATH(6),
    MAX_AGE_EXCEEDED(7),
    BIRTH_BEFORE_MARRIAGE_OF_PARENTS(8),
    BIRTH_BEFORE_DEATH_OF_PARENTS(9),
    MARRIED_BELOW_MIN_MARRIAGE_AGE(10),
    BIGAMY(11),
    PARENT_BIRTH_THRESHOLD_EXCEEDED(12),
    SIBLINGS_SPACING(13),
    MAX_MULTIPLE_SIMULTANEOUS_BIRTHS_EXCEEDED(14),
    MAX_FAMILY_CHILDREN_EXCEEDED(15),
    MALE_LAST_NAMES_DO_NOT_MATCH(16),
    MARRIAGE_TO_DESCENDANT(17),
    MARRIAGE_TO_SIBLING(18),
    MARRIAGE_TO_FIRST_COUSIN(19),
    MARRIAGE_TO_NIECE_NEPHEW(20),
    INCORRECT_GENDER_ROLE(21),
    ID_NOT_UNIQUE(22),
    NAME_BIRTH_NOT_UNIQUE(23),
    FAMILY_MARRIAGE_SPOUSES_NOT_UNIQUE(24),
    FAMILY_FIRSTNAMES_NOT_UNIQUE(25),
    CORRESPONDING_ENTRIES_NOT_FOUND(26),
    // US27-41 do not raise parsing errors
    ILLEGITIMATE_DATE(42);


    private final int code;
    private final String message;

    private Error(int code) {
        this.code = code;
        this.message = null;
    }

    private Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Error(int code, String format, Object... args) {
        this.code = code;
        this.message = String.format(format, args);
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return (message == null) ? this.toString() : message.toUpperCase();
    }

    @Override
    public String toString() {
        return super.toString().replace("_", " ");
    }

}
