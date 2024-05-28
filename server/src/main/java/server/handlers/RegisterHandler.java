package server.handlers;

import dataaccess.*;
import model.*;
import service.*;
import spark.*;
import com.google.gson.Gson;

public class RegisterHandler {

    private final Gson serializer = new Gson();
    private final ErrorHandler errorHandler = new ErrorHandler();

    public Object register(Request req, Response res, UserService userService) {
        res.type("application/json");
        try {
            AuthData newAuth = userService.createUser(serializer.fromJson(req.body(), UserData.class));
            res.status(200);
            return serializer.toJson(newAuth);
        } catch (BadRequestException e) {
            return errorHandler.handleError(e, res, 400);
        } catch (UserTakenException e) {
            return errorHandler.handleError(e, res, 403);
        } catch (Exception e) {
            return errorHandler.handleError(e, res, 500);
        }
    }
}
