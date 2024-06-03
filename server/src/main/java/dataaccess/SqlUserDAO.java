package dataaccess;

import model.UserData;

public class SqlUserDAO implements IntUserDAO {

    public SqlUserDAO() {
        configureDb();
    }

    public void createUser(UserData user) throws DataAccessException {

    }

    public UserData readUser(String username) throws DataAccessException {
        return null;
    }

    public void clear() throws DataAccessException {

    }

    private void configureDb() {

    }
}
