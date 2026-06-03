package com.critmon.watchdog.exception;

public class MonitorConflictException extends RuntimeException {

private final String errorCode;
    public MonitorConflictException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
