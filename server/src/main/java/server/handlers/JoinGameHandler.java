package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.BadRequestException;
import dataaccess.UserTakenException;
import dataaccess.UnauthorizedException;
import service.GameService;
import spark.Response;
import spark.Request;

public class JoinGameHandler {

    private final Gson serializer = new Gson();
    private final ErrorHandler errorHandler = new ErrorHandler();

    public Object joinGame(Request req, Response res, GameService gameService) {
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
}

