package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemUserDAO implements IntUserDAO {

    private final Map<String, UserData> users = new HashMap<>();

    public MemUserDAO() {
    }

    public void createUser(UserData user) {
        users.put(user.username(), user);
    }

    public UserData readUser(String username) {
        return users.get(username);
    }

    public void clear() throws DataAccessException {
        users.clear();
    }
}
