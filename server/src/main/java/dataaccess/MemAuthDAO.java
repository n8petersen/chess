package dataaccess;

import model.AuthData;

public class MemAuthDAO implements IntAuthDAO {

    public MemAuthDAO() {
    }

    public AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    public AuthData readAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public void clear() throws DataAccessException {

    }
}
