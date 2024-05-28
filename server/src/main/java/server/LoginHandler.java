package server;

import dataaccess.*;
import model.*;
import service.*;
import spark.*;
import com.google.gson.Gson;

public class LoginHandler {

    private final Gson serializer = new Gson();
    private final ErrorHandler errorHandler = new ErrorHandler();

    public Object login(Request req, Response res, UserService userService) {
        res.type("application/json");
        try {
            UserData user = serializer.fromJson(req.body(), UserData.class);
            AuthData auth = userService.loginUser(user);
            res.status(200);
            return serializer.toJson(auth);
        } catch (UnauthorizedException e) {
            return errorHandler.handleError(e, res, 401);
        } catch (Exception e) {
            return errorHandler.handleError(e, res, 500);
        }
    }
}

