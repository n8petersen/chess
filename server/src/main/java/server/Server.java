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

        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object Error(Exception e, Response res, int statusCode) {
        String body = serializer.toJson(Map.of("message", "Error: " + e.getMessage(), "success", false));
        res.type("application/json");
        res.status(statusCode);
        return body;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) {
        res.type("application/json");
        try {
            AuthData newAuth = userService.createUser(serializer.fromJson(req.body(), UserData.class));
            res.status(200);
            return serializer.toJson(newAuth);
        } catch (BadRequestException e) {
            return Error(e, res, 400);
        } catch (UserTakenException e) {
            return Error(e, res, 403);
        } catch (Exception e) {
            return Error(e, res, 500);
        }
    }

    private Object login(Request req, Response res) {
        res.type("application/json");
        try {
            UserData user = serializer.fromJson(req.body(), UserData.class);
            AuthData auth = userService.loginUser(user);
            res.status(200);
            return serializer.toJson(auth);
        } catch (UnauthorizedException e) {
            return Error(e, res, 401);
        } catch (Exception e) {
            return Error(e, res, 500);
        }
    }

    private Object logout(Request req, Response res) {
        res.type("application/json");
        try {
            String authToken = req.headers("Authorization");
            userService.logoutUser(authToken);
            res.status(200);
            return serializer.toJson(new Object());
        } catch (UnauthorizedException e) {
            return Error(e, res, 401);
        } catch (Exception e) {
            return Error(e, res, 500);
        }
    }

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
            return Error(e, res, 401);
        } catch (Exception e) {
            return Error(e, res, 500);
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
            return Error(e, res, 400);
        } catch (UnauthorizedException e) {
            return Error(e, res, 401);
        } catch (Exception e) {
            return Error(e, res, 500);
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
                return Error(e, res, 400);
            }
            gameService.joinGame(authToken, gameId, playerColor);
            return serializer.toJson(new Object());
        } catch (BadRequestException e) {
            return Error(e, res, 400);
        } catch (UserTakenException e) {
            return Error(e, res, 403);
        } catch (UnauthorizedException e) {
            return Error(e, res, 401);
        } catch (Exception e) {
            return Error(e, res, 500);
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
            return Error(e, res, 500);
        }
    }
}
