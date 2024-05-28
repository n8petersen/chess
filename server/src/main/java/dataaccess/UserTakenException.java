package dataaccess;

/**
 * Indicates provided username already exists
 */
public class UserTakenException extends Exception {
    public UserTakenException(String message) {
        super(message);
    }
}
