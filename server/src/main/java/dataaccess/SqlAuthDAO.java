package dataaccess;

import model.AuthData;

public class SqlAuthDAO implements IntAuthDAO {

    public SqlAuthDAO() {
        configureDb();
    }

    public AuthData createAuth(String username) throws DataAccessException {
        AuthData newAuth = new AuthData(null, null);
        return newAuth;
    }

    public AuthData readAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public void clear() throws DataAccessException {

    }

    private void configureDb() {

    }

}
