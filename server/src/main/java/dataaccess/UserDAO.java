package dataaccess;

import model.UserData;

public interface UserDAO {
    int createUser(UserData user) throws DataAccessException;
    UserData readUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
