package service;

import dataaccess.DataAccessException;
import dataaccess.IntUserDAO;
import model.UserData;

public class UserService {

    private final IntUserDAO userDataAccess;

    public UserService(IntUserDAO userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    public void createUser(UserData user) throws DataAccessException {
        userDataAccess.createUser(user);
    }

    public UserData getUser(String username) throws DataAccessException {
        return userDataAccess.readUser(username);
    }

    public void deleteUser(String username) throws DataAccessException {
        userDataAccess.deleteUser(username);
    }

    public void clearUsers() throws DataAccessException {
        userDataAccess.clear();
    }
}
