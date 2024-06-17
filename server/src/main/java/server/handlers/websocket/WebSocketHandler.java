package server.handlers.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocket.messages.*;

@WebSocket
public class WebSocketHandler {
    private final DataAccess dataAccess;
    private final ConnectionManager connectionManager = new ConnectionManager();

    public WebSocketHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @OnWebSocketConnect
    public void onConnect(Session ignoredsession) {
    }

    @OnWebSocketClose
    public void onClose(Session session, int ignoredStatusCode, String ignoredReason) {
        connectionManager.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
            var command = new Gson().fromJson(message, UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> connect(command, session);
                case MAKE_MOVE -> move();
                case LEAVE -> leave();
                case RESIGN -> resign();
            }

        } catch (Exception e) {
            Connection.sendError(session.getRemote(), e.getMessage());
        }
    }

    private void connect(UserGameCommand command, Session session) throws Exception {
        var authData = dataAccess.authDAO().readAuth(command.getAuthString());
        var userData = dataAccess.userDAO().readUser(authData.username());
        var gameData = dataAccess.gameDAO().readGame(command.getGameID());
        var connection = new Connection(userData, session);
        connection = connectionManager.add(userData.username(), connection);

        if (gameData != null) {
            connection.gameData = gameData;
            var loadMessage = new LoadMessage(gameData).toString();
            connection.send(loadMessage);
            String notificationMsg;

            String teamColor;
            if (userData.username().equalsIgnoreCase(gameData.whiteUsername())) {
                teamColor = "white";
            } else if (userData.username().equalsIgnoreCase(gameData.blackUsername())) {
                teamColor = "black";
            } else {
                teamColor = "observer";
            }

            notificationMsg = (new NotificationMessage(String.format("%s joined game '%s' as %s", connection.userData.username(), gameData.gameName(), teamColor))).toString();
            connectionManager.broadcast(gameData.gameID(), connection.userData.username(), notificationMsg);
        } else {
            connection.sendError("Couldn't find game");
        }
    }

    private void move() {
    }

    private void leave() {
    }

    private void resign() {
    }


}
