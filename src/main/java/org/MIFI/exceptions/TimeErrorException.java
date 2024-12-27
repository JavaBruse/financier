package org.MIFI.exceptions;

public class TimeErrorException  extends RuntimeException {
    public TimeErrorException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
