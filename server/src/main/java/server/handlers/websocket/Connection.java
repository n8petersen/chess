package server.handlers.websocket;

import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

public class Connection {
    public UserData userData;
    public GameData gameData;
    public Session session;

    public Connection(UserData user, Session session) {
        this.userData = user;
        this.session = session;
    }

    void send(String message) throws Exception {
        session.getRemote().sendString(message);
    }

    void sendError(String message) throws Exception {
        sendError(session.getRemote(), message);
    }

    static void sendError(RemoteEndpoint endpoint, String message) throws Exception {
        var errorMessage = (new ErrorMessage(String.format("ERROR: %s", message))).toString();
        endpoint.sendString(errorMessage);
    }

    @Override
    public String toString() {
        if (gameData != null) {
            return "Connection{" +
                    "user=" + userData.username() +
                    ", game=" + gameData.gameID() +
                    '}';
        } else {
            return "Connection{" +
                    "user=" + userData.username() +
                    '}';
        }

    }
}
