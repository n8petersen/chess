package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemAuthDAO implements IntAuthDAO {

    private final Map<String, AuthData> auths = new HashMap<>();

    public MemAuthDAO() {
    }

    public AuthData createAuth(String username) throws DataAccessException {
         String newAuthToken = UUID.randomUUID().toString();
         AuthData newAuth = new AuthData(newAuthToken, username);
         auths.put(newAuthToken, newAuth);
         return newAuth;
    }

    public AuthData readAuth(String authToken) throws DataAccessException {
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        auths.remove(authToken);
    }

    public void clear() throws DataAccessException {
        auths.clear();
    }
}
