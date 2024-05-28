package server.handlers;

import com.google.gson.Gson;
import dataaccess.UnauthorizedException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {

    private final Gson serializer = new Gson();
    private final ErrorHandler errorHandler = new ErrorHandler();

    public Object logout(Request req, Response res, UserService userService) {
        res.type("application/json");
        try {
            String authToken = req.headers("Authorization");
            userService.logoutUser(authToken);
            res.status(200);
            return serializer.toJson(new Object());
        } catch (UnauthorizedException e) {
            return errorHandler.handleError(e, res, 401);
        } catch (Exception e) {
            return errorHandler.handleError(e, res, 500);
        }
    }
}

