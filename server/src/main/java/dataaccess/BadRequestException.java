package dataaccess;

/**
 * Indicates provided username already exists
 */
public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
