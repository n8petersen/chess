package dataaccess;

import model.AuthData;

public interface IntAuthDAO {
    AuthData createAuth(String username) throws DataAccessException;
    AuthData readAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
}
