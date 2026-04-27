package com.infyme.digitalbanking.exception;

public enum ExceptionConstants {
    SERVER_ERROR("server.invalid"),
    AUTHENTICATION_FAILED("authentication.failed"),
    USER_NOT_FOUND("user.not.found"),
    USERID_NOT_FOUND("user.id.not.found"),
    NO_USERS_FOUND("no.users.found"),
    NO_ACCOUNTS_FOUND("no.account.found"),
    NO_ACCOUNT_IS_LINKED("no.account.is.linked"),
    INSUFFICIENT_FUNDS("insufficient.funds"),
    NO_ACTIVE_TRANSACTIONS("no.active.transactions"),
    OTP_DOESNOT_MATCH("otp.doesnot.match");

    private final String code;

    ExceptionConstants(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
