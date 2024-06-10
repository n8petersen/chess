package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    String username = "testUser";
    String pass = "testPass";
    String email = "testEmail";
    String authToken;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("localhost", port);
    }

    @BeforeEach
    public void clear() throws Exception {
        serverFacade.clear();
        authToken = serverFacade.register(username, pass, email).authToken();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerGood() throws Exception {
        String username = "newUser";
        String pass = "newPass";
        String email = "newEmail";
        AuthData newAuthData = serverFacade.register(username, pass, email);
        assertNotNull(newAuthData.authToken());
        assertEquals(newAuthData.username(), username);
    }

    @Test
    public void registerMissingEmail() {
        assertThrows(NullPointerException.class,
            () -> serverFacade.register(username, pass, null)
        );
    }

    @Test
    public void loginGood() throws Exception {
        AuthData authData = serverFacade.login(username, pass);
        assertEquals(authData.username(), username);
    }

    @Test
    public void loginBadPassword() {
        assertThrows(IOException.class,
            () -> {
                serverFacade.register(username, pass, email);
                serverFacade.login(username, "garbage");
            }
        );
    }

    @Test
    public void logoutGood() throws Exception {
        serverFacade.logout(authToken);
    }

    @Test
    public void logoutBadAuthToken() {
        assertThrows(IOException.class,
            () -> serverFacade.logout("garbage")
        );
    }

    @Test
    public void createGood() throws Exception {
        String game = "testGame1";
        String game2 = "testGame2";
        GameData gameData1 = serverFacade.createGame(authToken, game);
        GameData gameData2 = serverFacade.createGame(authToken, game2);
        assertEquals(1, gameData1.gameID());
        assertEquals(2, gameData2.gameID());
    }

    @Test
    public void createMissingName() {
        assertThrows(NullPointerException.class,
            () -> serverFacade.createGame(authToken, null)
        );
    }

    @Test
    public void listEmpty() throws Exception {
        var games = serverFacade.listGames(authToken).games();
        assertEquals(0, games.length);
    }

    @Test
    public void listOneGame() throws Exception {
        serverFacade.createGame(authToken, "testGame");
        var games = serverFacade.listGames(authToken).games();
        assertEquals(1, games.length);
    }

    @Test
    public void listMultipleGames() throws Exception {
        serverFacade.createGame(authToken, "testGame1");
        serverFacade.createGame(authToken, "testGame2");
        serverFacade.createGame(authToken, "testGame3");
        var games = serverFacade.listGames(authToken).games();
        assertEquals(3, games.length);
    }

    @Test
    public void joinGood() throws Exception {
        GameData gameData = serverFacade.createGame(authToken, "testGame");
        gameData = serverFacade.joinGame(authToken, gameData.gameID(), ChessGame.TeamColor.WHITE);
        assertEquals(gameData.whiteUsername(), username);
    }

    @Test
    public void joinTaken() throws Exception {
        AuthData newUser = serverFacade.register("user1", "pass", "email");
        GameData gameData = serverFacade.createGame(authToken, "testGame");
        gameData = serverFacade.joinGame(newUser.authToken(), gameData.gameID(), ChessGame.TeamColor.WHITE);
        GameData finalGameData = gameData;
        assertThrows(IOException.class,
            () -> serverFacade.joinGame(authToken, finalGameData.gameID(), ChessGame.TeamColor.WHITE)
        );
    }

}
