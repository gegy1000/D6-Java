package net.gegy1000.communicator.exception;

/**
 * Request thrown for an invalid D6 request
 */
public class InvalidRequestException extends SchoolException {
    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(Exception cause) {
        super(cause);
    }
}
