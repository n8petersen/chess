package server;

import com.google.gson.Gson;
import dataaccess.*;
import service.*;
import model.*;
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

        this.gameService = new GameService(gameDao);
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

    private Object Error(Exception e, Request req, Response res, int statusCode) {
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
            AuthData newAuth =userService.createUser(serializer.fromJson(req.body(), UserData.class));
            res.status(200);
            return serializer.toJson(newAuth);
        } catch (BadRequestException e) {
            return Error(e, req, res, 400);
        } catch (UserTakenException e) {
            return Error(e, req, res, 403);
        } catch (Exception e) {
            return Error(e, req, res, 500);
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
            return Error(e, req, res, 401);
        } catch (Exception e) {
            return Error(e, req, res, 500);
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
            return Error(e, req, res, 401);
        } catch (Exception e) {
            return Error(e, req, res, 500);
        }

    }

    private Object listGames(Request req, Response res) {
        System.out.println("List Games");
        System.out.println(req.body());
        return new Object();
    }

    private Object createGame(Request req, Response res) {
        System.out.println("Create game");
        System.out.println(req.body());
        return new Object();
    }

    private Object joinGame(Request req, Response res) {
        System.out.println("Join Game");
        System.out.println(req.body());
        return new Object();
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
            return Error(e, req, res, 500);
        }
    }
}
