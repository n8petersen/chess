package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private IntGameDAO gameDao;
    private IntAuthDAO authDAO;
    private IntUserDAO userDAO;
    private UserService userService;

    @BeforeEach
    void setup() {
        gameDao = new MemGameDAO();
        authDAO = new MemAuthDAO();
        userDAO = new MemUserDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    void createUser() throws UserTakenException, BadRequestException, DataAccessException {
        UserData userData = new UserData("user", "password", "email");
        userService.createUser(userData);
        UserData checkUser = userDAO.readUser("user");
        assertEquals(userData, checkUser);
    }

    @Test
    void createTwoUsers() throws UserTakenException, BadRequestException, DataAccessException {
        UserData userData1 = new UserData("user1", "password", "email");
        UserData userData2 = new UserData("user2", "password", "email");
        userService.createUser(userData1);
        userService.createUser(userData2);
        UserData checkUser1 = userDAO.readUser("user1");
        UserData checkUser2 = userDAO.readUser("user2");
        assertNotEquals(checkUser1, checkUser2);
    }

    @Test
    void createUserAlreadyTaken() {
        assertThrows(UserTakenException.class,
                () -> {
                    UserData userData1 = new UserData("user", "password", "email");
                    UserData userData2 = new UserData("user", "password", "email");
                    userService.createUser(userData1);
                    userService.createUser(userData2);
                }
        );

    }

    @Test
    void loginUser() throws DataAccessException, UnauthorizedException {
        UserData userData1 = new UserData("user1", BCrypt.hashpw("password", BCrypt.gensalt()), "email");
        userDAO.createUser(userData1);
        UserData checkUser = new UserData("user1", "password", null);
        AuthData authData = userService.loginUser(checkUser);
        assertNotNull(authData);
    }

    @Test
    void loginUserDoesntExist() {
        assertThrows(UnauthorizedException.class,
                () -> {
                    UserData checkUser = new UserData("user1", "password", null);
                    AuthData authData = userService.loginUser(checkUser);
                    assertNotNull(authData);
                }
        );
    }

    @Test
    void logoutUser() throws DataAccessException, UnauthorizedException {
        AuthData newAuth = authDAO.createAuth("testUser");
        userService.logoutUser(newAuth.authToken());
    }

    @Test
    void logoutUserBadToken() {
        assertThrows(UnauthorizedException.class,
                () -> {
                    authDAO.createAuth("testUser");
                    userService.logoutUser("garbage");
                }
        );
    }

    @Test
    void clearUsersAuths() throws DataAccessException {
        gameDao.createGame("gameTest");
        AuthData newAuth = authDAO.createAuth("auth1");
        userDAO.createUser(new UserData("user1", "password1", "email1"));

        userService.clearUsersAuths();

        Collection<GameData> gameList = gameDao.readAllGames();
        UserData checkUser = userDAO.readUser("user1");
        AuthData checkAuth = authDAO.readAuth(newAuth.authToken());

        assertNotEquals(0, gameList.size());
        assertNull(checkUser);
        assertNull(checkAuth);
    }
}