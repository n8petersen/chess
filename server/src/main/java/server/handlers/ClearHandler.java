package server.handlers;

import com.google.gson.Gson;
import spark.Response;
import service.GameService;
import service.UserService;

public class ClearHandler {

    private final Gson serializer = new Gson();
    private final ErrorHandler errorHandler = new ErrorHandler();

    public Object clear(Response res, UserService userService, GameService gameService) {
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
