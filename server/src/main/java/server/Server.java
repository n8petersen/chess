package server;

import com.google.gson.*;
import dataaccess.*;
import service.*;
import model.*;

import java.util.Collection;
import java.util.Map;

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
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    // Need to move these handlers into their own classes

    // Need to add unit tests to Service classes https://github.com/softwareconstruction240/softwareconstruction/blob/main/chess/3-web-api/web-api.md#service-unit-tests
    //  "Each public method on your Service classes has two test cases, one positive test and one negative test. Every test case includes an Assert statement of some type"


    private Object listGames(Request req, Response res) {
        res.type("application/json");
        try {
            String authToken = req.headers("Authorization");
            Collection<GameData> games = gameService.listGames(authToken);

            Gson gson = new GsonBuilder().serializeNulls().create();
            JsonArray jsonArray = gson.toJsonTree(games).getAsJsonArray();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("games", jsonArray);
            res.status(200);
            return serializer.toJson(jsonObject);
        } catch (UnauthorizedException e) {
            return errorHandler.handleError(e, res, 401);
        } catch (Exception e) {
            return errorHandler.handleError(e, res, 500);
        }
    }

    private Object createGame(Request req, Response res) {
        res.type("application/json");
        try {
            String authToken = req.headers("Authorization");
            GameData newGameData = serializer.fromJson(req.body(), GameData.class);
            int newGameId = gameService.createNewGame(newGameData, authToken).gameID();
            res.status(200);
            return serializer.toJson(Map.of("gameID", newGameId));
        } catch (BadRequestException e) {
            return errorHandler.handleError(e, res, 400);
        } catch (UnauthorizedException e) {
            return errorHandler.handleError(e, res, 401);
        } catch (Exception e) {
            return errorHandler.handleError(e, res, 500);
        }
    }

    private Object joinGame(Request req, Response res) {
        res.type("application/json");
        try {
            String authToken = req.headers("Authorization");

            JsonElement bodyJsonElement = JsonParser.parseString(req.body());
            JsonObject jsonObject = bodyJsonElement.getAsJsonObject();
            String playerColor;
            int gameId;
            try {
                playerColor = jsonObject.get("playerColor").getAsString();
                gameId = jsonObject.get("gameID").getAsInt();
            } catch (Exception e) {
                return errorHandler.handleError(e, res, 400);
            }
            gameService.joinGame(authToken, gameId, playerColor);
            return serializer.toJson(new Object());
        } catch (BadRequestException e) {
            return errorHandler.handleError(e, res, 400);
        } catch (UserTakenException e) {
            return errorHandler.handleError(e, res, 403);
        } catch (UnauthorizedException e) {
            return errorHandler.handleError(e, res, 401);
        } catch (Exception e) {
            return errorHandler.handleError(e, res, 500);
        }
    }

    private Object clear(Request req, Response res) {
        res.type("application/json");
        try {
            userService.clearUsersAuths();
            gameService.clearGames();
            res.status(200);
            res.body("");
            return serializer.toJson(new Object());
        } catch (Exception e) {
            return errorHandler.handleError(e, res, 500);
        }
    }
}
