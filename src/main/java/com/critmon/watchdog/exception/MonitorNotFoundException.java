package com.critmon.watchdog.exception;

public class MonitorNotFoundException extends RuntimeException {

    public MonitorNotFoundException(String id) {
        super("No monitor found with id '" + id + "'");
    }
}
