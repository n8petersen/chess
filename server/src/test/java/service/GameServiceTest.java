package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private IntGameDAO gameDao;
    private IntAuthDAO authDAO;
    private IntUserDAO userDAO;
    private AuthData authData;
    private GameService gameService;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDao = new MemGameDAO();
        authDAO = new MemAuthDAO();
        userDAO = new MemUserDAO();
        authData = authDAO.createAuth("testUser");
        gameService = new GameService(gameDao, authDAO);
    }

    @Test
    @Order(1)
    void createNewGameWithGoodData() throws DataAccessException, UnauthorizedException, BadRequestException {
        GameData testGame = new GameData(0, null, null, "gameTest", null);
        testGame = gameService.createNewGame(testGame, authData.authToken());
        assertNotEquals(0, testGame.gameID());
        assertEquals(101, testGame.gameID());
    }

    @Test
    @Order(2)
    void createNewGameBadAuthToken() {
        assertThrows(UnauthorizedException.class,
                () -> {
                    GameService gameService = new GameService(gameDao, authDAO);
                    GameData gameData = new GameData(0, null, null, "gameTest", null);
                    gameService.createNewGame(gameData, "garbage");
                }
        );
    }

    @Test
    void ListGamesOneGame() throws UnauthorizedException, DataAccessException {
        gameDao.createGame("gameTest");
        Collection<GameData> gameList = gameService.listGames(authData.authToken());
        assertNotEquals(0, gameList.size());
        assertEquals(1, gameList.size());
    }

    @Test
    void ListGamesBadAuthToken() {
        assertThrows(UnauthorizedException.class,
                () -> {
                    gameDao.createGame("gameTest");
                    gameService.listGames("garbage");
                }
        );
    }

    @Test
    void joinGameWithUserOnWhite() throws DataAccessException, UserTakenException, UnauthorizedException, BadRequestException {
        GameData testGame = new GameData(0, null, null, "gameTest", null);
        testGame = gameService.createNewGame(testGame, authData.authToken());
        gameService.joinGame(authData.authToken(), testGame.gameID(), "WHITE");
        GameData checkGame = gameDao.readGame(testGame.gameID());
        assertEquals("testUser", checkGame.whiteUsername());
    }

    @Test
    void joinGameBadAuthToken() {
        assertThrows(UnauthorizedException.class,
                () -> {
                    GameData testGame = new GameData(0, null, null, "gameTest", null);
                    testGame = gameService.createNewGame(testGame, authData.authToken());
                    gameService.joinGame("garbage", testGame.gameID(), "WHITE");
                }
        );
    }

    @Test
    void clearGamesOnlyGames() throws DataAccessException, UnauthorizedException {
        gameDao.createGame("gameTest");
        gameDao.createGame("gameTest2");
        AuthData newAuth = authDAO.createAuth("auth1");
        userDAO.createUser(new UserData("user1", "password1", "email1"));
        gameService.clearGames();
        Collection<GameData> gameList = gameService.listGames(authData.authToken());
        UserData checkUser = userDAO.readUser("user1");
        AuthData checkAuth = authDAO.readAuth(newAuth.authToken());
        assertEquals(0, gameList.size());
        assertNotNull(checkUser);
        assertNotNull(checkAuth);
    }
}