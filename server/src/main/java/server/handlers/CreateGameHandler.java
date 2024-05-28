package server.handlers;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import model.GameData;
import service.GameService;
import spark.Response;
import spark.Request;

import java.util.Map;

public class CreateGameHandler {

    private final Gson serializer = new Gson();
    private final ErrorHandler errorHandler = new ErrorHandler();

    public Object createGame(Request req, Response res, GameService gameService) {
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
}

