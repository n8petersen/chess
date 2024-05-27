package service;

import dataaccess.DataAccessException;
import dataaccess.IntAuthDAO;
import model.AuthData;
import model.UserData;

public class AuthService {

    private final IntAuthDAO authDataAccess;

    public AuthService(IntAuthDAO authDataAccess) {
        this.authDataAccess = authDataAccess;
    }

    public AuthData createAuth(UserData user)  throws DataAccessException{
        return authDataAccess.createAuth(user.username());
    }

    public AuthData getAuth(String authToken)  throws DataAccessException{
        return authDataAccess.readAuth(authToken);
    }

    public void deleteAuth(String authToken)  throws DataAccessException{
        authDataAccess.deleteAuth(authToken);
    }

    public void clearAuths() throws DataAccessException {
        authDataAccess.clear();
    }
}