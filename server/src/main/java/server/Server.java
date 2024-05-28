package server;

import com.google.gson.*;
import dataaccess.*;
import server.handlers.*;
import service.*;

import spark.*;

public class Server {

    private final GameService gameService;
    private final UserService userService;
    private final Gson serializer = new Gson();
    private final ErrorHandler errorHandler = new ErrorHandler();

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
        Spark.delete("/db", (req, res) -> new ClearHandler().clear(req, res, userService, gameService));

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
