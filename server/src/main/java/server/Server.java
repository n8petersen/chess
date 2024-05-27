package server;

import com.google.gson.Gson;
import dataaccess.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import java.util.Map;
import spark.*;

import javax.xml.crypto.Data;

public class Server {

    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;
    private final Gson serializer = new Gson();

    public Server() {
        var authDao = new MemAuthDAO();
        var gameDao = new MemGameDAO();
        var userDao = new MemUserDAO();

        this.authService = new AuthService(authDao);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

//        Spark.post("/user", this::register);
//        Spark.post("/session". this::login);
//        Spark.delete("/session", this::logout);
//        Spark.get("/game", this::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object Error(Exception e, Request req, Response res) {
        String body = serializer.toJson(Map.of("message", "Error: " + e.getMessage(), "success", false));
        res.type("application/json");
        res.status(500);
        return body;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) {
        res.type("application/json");
        try {
            authService.clearAuths();
            gameService.clearGames();
            userService.clearUsers();
            res.status(200);
            return "";
        } catch (DataAccessException e) {
            return Error(e, req, res);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
