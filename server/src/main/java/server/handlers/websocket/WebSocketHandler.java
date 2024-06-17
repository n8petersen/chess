package server.handlers.websocket;

import com.google.gson.Gson;
import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;

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
                case CONNECT -> connect();
                case MAKE_MOVE -> move();
                case LEAVE -> leave();
                case RESIGN -> resign();
            }

        } catch (Exception e) {
            Connection.sendError(session.getRemote(), e.getMessage());
        }
    }

    private void connect() {
    }

    private void move() {
    }

    private void leave() {
    }

    private void resign() {
    }


}
