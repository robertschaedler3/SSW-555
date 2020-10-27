package gedcom.logging;

public enum Error {
    DATES_BEFORE_CURRENT_DATE(ErrorType.ERROR, 1),
    BIRTH_BEFORE_MARRIAGE(ErrorType.ERROR, 2),
    BIRTH_BEFORE_DEATH(ErrorType.ERROR, 3),
    MARRIAGE_BEFORE_DIVORCE(ErrorType.ERROR, 4),
    MARRIAGE_BEFORE_DEATH(ErrorType.ERROR, 5),
    DIVORCE_BEFORE_DEATH(ErrorType.ERROR, 6),
    MAX_AGE_EXCEEDED(ErrorType.ANOMALY, 7),
    BIRTH_BEFORE_MARRIAGE_OF_PARENTS(ErrorType.ANOMALY, 8),
    BIRTH_BEFORE_DEATH_OF_PARENTS(ErrorType.ERROR, 9),
    MARRIED_BELOW_MIN_MARRIAGE_AGE(ErrorType.ANOMALY, 10),
    BIGAMY(ErrorType.ANOMALY, 11),
    PARENT_BIRTH_THRESHOLD_EXCEEDED(ErrorType.ANOMALY, 12),
    SIBLINGS_SPACING(ErrorType.ANOMALY, 13),
    MAX_MULTIPLE_SIMULTANEOUS_BIRTHS_EXCEEDED(ErrorType.ANOMALY, 14),
    MAX_FAMILY_CHILDREN_EXCEEDED(ErrorType.ERROR, 15),
    MALE_LAST_NAMES_DO_NOT_MATCH(ErrorType.ANOMALY, 16),
    MARRIAGE_TO_DESCENDANT(ErrorType.ANOMALY, 17),
    MARRIAGE_TO_SIBLING(ErrorType.ANOMALY, 18),
    MARRIAGE_TO_FIRST_COUSIN(ErrorType.ANOMALY, 19),
    MARRIAGE_TO_NIECE_NEPHEW(ErrorType.ANOMALY, 20),
    INCORRECT_GENDER_ROLE(ErrorType.ERROR, 21),
    INDIVIDUAL_ID_NOT_UNIQUE(ErrorType.ERROR, 22),
    NAME_BIRTH_NOT_UNIQUE(ErrorType.ANOMALY, 23),
    FAMILY_MARRIAGE_SPOUSES_NOT_UNIQUE(ErrorType.ERROR, 24),
    FAMILY_FIRSTNAMES_NOT_UNIQUE(ErrorType.ANOMALY, 25),
    CORRESPONDING_ENTRIES_NOT_FOUND(ErrorType.ERROR, 26),
    // US27-41 do not raise parsing errors
    ILLEGITIMATE_DATE(ErrorType.ERROR, 42);


    private final ErrorType errorType;
    private final int code;

    private Error(ErrorType errorType, int code) {
        this.errorType = errorType;
        this.code = code;
    }

    public ErrorType type() {
        return this.errorType;
    }

    public int code() {
        return this.code;
    }

}
