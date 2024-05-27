package service;

import dataaccess.DataAccessException;
import dataaccess.IntUserDAO;
import model.UserData;

public class UserService {

    private final IntUserDAO userDataAccess;

    public UserService(IntUserDAO userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    public void createUser(UserData user) {
        try {
            userDataAccess.createUser(user);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public UserData getUser(String username) {
        try {
            return userDataAccess.readUser(username);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(String username) {
        try {
            userDataAccess.deleteUser(username);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearUsers() {
        try {
            userDataAccess.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
