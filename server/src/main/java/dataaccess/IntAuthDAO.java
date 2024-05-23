package dataaccess;

import model.AuthData;

public interface IntAuthDAO {

    /**
     * Create a new authToken for a given user
     * @param username - Username of the user for which to create AuthData
     * @return AuthData which was newly created for the user
     * @throws DataAccessException for DB access violations
     */
    AuthData createAuth(String username) throws DataAccessException;

    /**
     * Read the AuthData for an existing authToken
     * @param authToken - authToken for the AuthData we want to retrieve
     * @return AuthData
     * @throws DataAccessException for DB access violations
     */
    AuthData readAuth(String authToken) throws DataAccessException;

    /**
     * Delete the AuthData for an existing authToken
     * @param authToken - authToken for the AuthData we want to delete
     * @throws DataAccessException for DB access violations
     */
    void deleteAuth(String authToken) throws DataAccessException;

    /**
     * Clears all AuthData instances
     * @throws DataAccessException for DB access violations
     */
    void clear() throws DataAccessException;
}
