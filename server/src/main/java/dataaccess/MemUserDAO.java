package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemUserDAO implements IntUserDAO {

    private final Map<String, UserData> users = new HashMap<>();

    public MemUserDAO() {
    }

    public UserData createUser(UserData user) {
        users.put(user.username(), user);
        return user;
    }

    public UserData readUser(String username) {
        return users.get(username);
    }

    public void clear() {
        users.clear();
    }
}
