package com.fpt.capstone.backend.api.BackEnd.service.validate;

public enum ConstantsRegex {
    USERNAME_PATTERN("^[a-z0-9._-]{5,20}$"),
    NAME_PATTERN("^[a-zA-Z0-9\\s]{5,255}$"),
    FULLNAME_PATTERN("^[a-zA-Z\\s]+"),
    GENDER_PATTERN("^(?i)(male|female)$"),
    STATUS_PATTERN("^(?i)(ACTIVE|INACTIVE)$"),
    FUNCTION_STATUS_PATTERN("^(?i)(PENDING|PLANNED|EVALUATED|REJECTED|DONE)$"),
    COMPLEXITY_STATUS_PATTERN("^(?i)(COMPLEX|MEDIUM|SIMPLE)$"),
    PRIORITY_STATUS_PATTERN("(?i)(URGENT|HIGHT|MEDIUM|LOW|LOWEST)$"),

    TEAM_EVALUATION_PATTERN("^(?i)(ACTIVE|INACTIVE)$"),

    /**
     * status for classes,...
     *
     * @STATUS02_PATTERN: status must be active, closed or cancelled!
     */

    STATUS02_PATTERN("^(?i)(ACTIVE|CLOSED|CANCELLED)$"),
    STATUS03_PATTERN("^(?i)(OPEN|CLOSED|CANCELLED)$"),
    PROJECTLEADER_PATTERN("^(?i)(0|1)$"),
    BLOCK5_PATTERN("^(?i)(0|1)$"),
    /**
     * @TERM_PATTERN: term must be spring, summer or winter!
     */
    TERM_PATTERN("^(?i)(SPRING|SUMMER|WINTER)$"),
    NUMBER_PATTERN("^\\d+$"),
    DATE_PATTERN("^([0-2][0-9]||3[0-1])-(0[0-9]||1[0-2])-([0-9][0-9])?[0-9][0-9]$"),
    DECIMAL_PATTERN("^(0|[1-9]\\d*)(\\.\\d+)?$"),
    DECIMAL2_PATTERN("^[0-9]\\.?[0-9]?[0-9]$"),
    PHONE_PATTERN("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}"),
    EMAIL_PATTERN("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"),
    /**
     * @CODE_PATTERN: Must have 3-50 character and don't have special character
     */
    CODE_PATTERN("^[a-zA-Z0-9._-]{3,50}$"),
    LINK_PATTERN("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"),
    /**
     * @PASSWORD_PATTERN: Minimum eight characters, at least one letter and one number
     */
    PASSWORD_PATTERN("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");

    private final String text;

    ConstantsRegex(String s) {
        text = s;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
