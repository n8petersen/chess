package service;

import dataaccess.*;
import dataaccess.dao.IntAuthDAO;
import dataaccess.dao.IntUserDAO;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private final IntUserDAO userDataAccess;
    private final IntAuthDAO authDataAccess;

    public UserService(IntUserDAO userDataAccess, IntAuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    /* public functions */
    public AuthData createUser(UserData user) throws DataAccessException, UserTakenException, BadRequestException {
        UserData checkUser = getUser(user.username());
        if (checkUser != null) {
            throw new UserTakenException("already taken");
        } else if (user.password() == null || user.password().isEmpty()) {
            throw new BadRequestException("bad request");
        } else {
            userDataAccess.createUser(user);
            return createAuth(user);
        }
    }

    public AuthData loginUser(UserData user) throws DataAccessException, UnauthorizedException {
        UserData checkUser = getUser(user.username());
        if (checkUser != null && BCrypt.checkpw(user.password(), checkUser.password())) {
            return authDataAccess.createAuth(user.username());
        } else {
            throw new UnauthorizedException("unauthorized");
        }
    }

    public void logoutUser(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData userAuth = getAuth(authToken);
        if (userAuth == null) {
            throw new UnauthorizedException("unauthorized");
        } else {
            deleteAuth(authToken);
        }
    }

    public void clearUsersAuths() throws DataAccessException {
        clearAuths();
        clearUsers();
    }

    /* User helper functions */
    private UserData getUser(String username) throws DataAccessException {
        return userDataAccess.readUser(username);
    }

    private void clearUsers() throws DataAccessException {
        userDataAccess.clear();
    }

    /* Auth helper functions */
    private AuthData createAuth(UserData user) throws DataAccessException {
        return authDataAccess.createAuth(user.username());
    }

    private AuthData getAuth(String authToken) throws DataAccessException {
        return authDataAccess.readAuth(authToken);
    }

    private void deleteAuth(String authToken) throws DataAccessException {
        authDataAccess.deleteAuth(authToken);
    }

    private void clearAuths() throws DataAccessException {
        authDataAccess.clear();
    }

}
