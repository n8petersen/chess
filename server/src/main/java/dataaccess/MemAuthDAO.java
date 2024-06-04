package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemAuthDAO implements IntAuthDAO {

    private final Map<String, AuthData> auths = new HashMap<>();

    public MemAuthDAO() {
    }

    public AuthData createAuth(String username) {
        AuthData newAuth = new AuthData(AuthData.createToken(), username);
        auths.put(newAuth.authToken(), newAuth);
        return newAuth;
    }

    public AuthData readAuth(String authToken) {
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    public void clear() {
        auths.clear();
    }
}
