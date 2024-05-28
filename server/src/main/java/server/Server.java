package server;

import dataaccess.MemAuthDAO;
import dataaccess.MemGameDAO;
import dataaccess.MemUserDAO;
import server.handlers.RegisterHandler;
import server.handlers.LoginHandler;
import server.handlers.LogoutHandler;
import server.handlers.ListGamesHandler;
import server.handlers.CreateGameHandler;
import server.handlers.JoinGameHandler;
import server.handlers.ClearHandler;
import service.GameService;
import service.UserService;
import spark.Spark;

public class Server {

    private final GameService gameService;
    private final UserService userService;

    public Server() {
        MemAuthDAO authDao = new MemAuthDAO();
        MemGameDAO gameDao = new MemGameDAO();
        MemUserDAO userDao = new MemUserDAO();

        this.gameService = new GameService(gameDao, authDao);
        this.userService = new UserService(userDao, authDao);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        Spark.post("/user", (req, res) -> new RegisterHandler().register(req, res, userService));
        Spark.post("/session", (req, res) -> new LoginHandler().login(req, res, userService));
        Spark.delete("/session", (req, res) -> new LogoutHandler().logout(req, res, userService));
        Spark.get("/game", (req, res) -> new ListGamesHandler().listGames(req, res, gameService));
        Spark.post("/game", (req, res) -> new CreateGameHandler().createGame(req, res, gameService));
        Spark.put("/game", (req, res) -> new JoinGameHandler().joinGame(req, res, gameService));
        Spark.delete("/db", (req, res) -> new ClearHandler().clear(res, userService, gameService));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    // Need to add unit tests to Service classes https://github.com/softwareconstruction240/softwareconstruction/blob/main/chess/3-web-api/web-api.md#service-unit-tests
    //  "Each public method on your Service classes has two test cases, one positive test and one negative test. Every test case includes an Assert statement of some type"

}
