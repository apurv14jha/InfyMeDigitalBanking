package com.infyme.digitalbanking.exception;

import java.time.LocalDateTime;

public class ErrorInformation {
    private String errorMessage;
    private String errorCode;
    private LocalDateTime errorTimeStamp;

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public LocalDateTime getErrorTimeStamp() { return errorTimeStamp; }
    public void setErrorTimeStamp(LocalDateTime errorTimeStamp) { this.errorTimeStamp = errorTimeStamp; }
}
