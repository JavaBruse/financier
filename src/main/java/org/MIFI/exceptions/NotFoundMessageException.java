package org.MIFI.exceptions;

public class NotFoundMessageException extends RuntimeException  {
    public NotFoundMessageException(String message) {
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
