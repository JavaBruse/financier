package org.MIFI.exceptions;

public class LimitIsOverException  extends RuntimeException{
    public LimitIsOverException(String message) {
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
