package dataaccess.dao;

import dataaccess.DataAccessException;
import model.UserData;

public interface IntUserDAO {

    /**
     * Create a new user given UserData
     *
     * @param user UserData object (username, password, email)
     * @throws DataAccessException for DB access violations
     */
    UserData createUser(UserData user) throws DataAccessException;

    /**
     * Read a user given its username
     *
     * @param username Username to retrieve
     * @return UserData object to retrieve
     * @throws DataAccessException for DB access violations
     */
    UserData readUser(String username) throws DataAccessException;

    /**
     * Delete all users
     *
     * @throws DataAccessException for DB access violations
     */
    void clear() throws DataAccessException;
}
