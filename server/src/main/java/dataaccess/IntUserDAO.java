package dataaccess;

import model.UserData;

public interface IntUserDAO {

    /**
     * Create a new user given UserData
     * @param user UserData object (username, password, email)
     * @return ID of user created
     * @throws DataAccessException for DB access violations
     */
    int createUser(UserData user) throws DataAccessException;

    /**
     * Read a user given its username
     * @param username Username to retrieve
     * @return UserData object to retrieve
     * @throws DataAccessException for DB access violations
     */
    UserData readUser(String username) throws DataAccessException;

    /**
     * Delete a user given its ID
     * @param userID ID of user to delete
     * @throws DataAccessException for DB access violations
     */
    void deleteUser(int userID) throws DataAccessException;

    /**
     * Delete all users
     * @throws DataAccessException for DB access violations
     */
    void clear() throws DataAccessException;
}
