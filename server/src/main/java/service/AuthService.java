package service;

import dataaccess.DataAccessException;
import dataaccess.IntAuthDAO;
import model.AuthData;
import model.UserData;

public class AuthService {

    private IntAuthDAO authDataAccess;

    private AuthService() {}

    public AuthData createAuth(UserData user) {
        try {
            return authDataAccess.createAuth(user.username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData getAuth(String authToken) {
        try {
            return authDataAccess.readAuth(authToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAuth(String authToken) {
        try {
            authDataAccess.deleteAuth(authToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearAuths() {
        try {
            authDataAccess.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}