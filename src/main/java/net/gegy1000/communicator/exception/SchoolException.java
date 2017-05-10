package net.gegy1000.communicator.exception;

/**
 * Generic D6 school exception
 */
public class SchoolException extends Exception {
    public SchoolException() {
        super();
    }

    public SchoolException(String message) {
        super(message);
    }

    public SchoolException(Exception cause) {
        super(cause);
    }

    public SchoolException(String message, Exception cause) {
        super(message, cause);
    }
}
