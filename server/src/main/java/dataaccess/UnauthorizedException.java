package dataaccess;

/**
 * Indicates provided username already exists
 */
public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super(message);
    }
}
