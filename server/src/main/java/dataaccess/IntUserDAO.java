package dataaccess;

import model.UserData;

public interface IntUserDAO {

    /**
     * Create a new user given UserData
     *
     * @param user UserData object (username, password, email)
     * @throws DataAccessException for DB access violations
     */
    void createUser(UserData user) throws DataAccessException;

    /**
     * Read a user given its username
     *
     * @param username Username to retrieve
     * @return UserData object to retrieve
     * @throws DataAccessException for DB access violations
     */
    UserData readUser(String username) throws DataAccessException;

    /**
     * Delete a user given its ID
     *
     * @param username username of user to delete
     * @throws DataAccessException for DB access violations
     */
    void deleteUser(String username) throws DataAccessException;

    /**
     * Delete all users
     *
     * @throws DataAccessException for DB access violations
     */
    void clear() throws DataAccessException;
}
