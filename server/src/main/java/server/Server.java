package server;

import com.google.gson.Gson;
import dataaccess.*;
import service.*;
import model.*;
import java.util.Map;
import spark.*;

public class Server {

    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;
    private final Gson serializer = new Gson();

    public Server() {
        MemAuthDAO authDao = new MemAuthDAO();
        MemGameDAO gameDao = new MemGameDAO();
        MemUserDAO userDao = new MemUserDAO();

        this.authService = new AuthService(authDao);
        this.gameService = new GameService(gameDao);
        this.userService = new UserService(userDao);
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
            System.out.println("Register user");
            System.out.println(req.body());
            UserData newUser = serializer.fromJson(req.body(), UserData.class);
            userService.createUser(newUser);
            AuthData newAuthData = authService.createAuth(newUser);
            res.status(200);
            return serializer.toJson(newAuthData);
        } catch (DataAccessException e) {
            return Error(e, req, res, 500);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object login(Request req, Response res) {
        System.out.println("Login user");
        System.out.println(req);
        return new Object();
    }

    private Object logout(Request req, Response res) {
        System.out.println("Logout user");
        System.out.println(req);
        return new Object();
    }

    private Object listGames(Request req, Response res) {
        System.out.println("List Games");
        System.out.println(req);
        return new Object();
    }

    private Object createGame(Request req, Response res) {
        System.out.println("Create game");
        System.out.println(req);
        return new Object();
    }

    private Object joinGame(Request req, Response res) {
        System.out.println("Join Game");
        System.out.println(req);
        return new Object();
    }

    private Object clear(Request req, Response res) {
//        res.type("application/json");
//        try {
//            authService.clearAuths();
//            gameService.clearGames();
//            userService.clearUsers();
//            res.status(200);
//            return "";
//        } catch (DataAccessException e) {
//            return Error(e, req, res);
//        }
//        catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        System.out.println("Clear App");
        System.out.println(req);
        return new Object();
    }
}
