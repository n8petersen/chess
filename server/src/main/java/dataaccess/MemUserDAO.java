package dataaccess;

import model.UserData;

public class MemUserDAO {

    public MemUserDAO() {
    }

    public int createUser(UserData user) throws DataAccessException {
        return 0;
    }

    public UserData readUser(String username) throws DataAccessException {
        return null;
    }

    public void deleteUser(int userID) throws DataAccessException {

    }

    public void clear() throws DataAccessException {

    }
}
