package server.handlers.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public Connection add(String username, Connection connection) {
        connections.put(username, connection);
        return connection;
    }

    public void remove(Session session) {
        for (var conn : connections.values()) {
            if (conn.session.equals(session)) {
                connections.remove(conn.userData.username());
            }
        }
    }

    public Connection get(String username) {
        return connections.get(username);
    }

    public void broadcast(int gameID, String sendingUser, String message) throws Exception {
        for (var conn : connections.values()) {
            if (conn.gameData.gameID() == gameID && !sendingUser.equals(conn.userData.username())) {
                conn.send(message);
            }
        }
    }

    @Override
    public String toString() {
        return "ConnectionManager{" +
                "connections=" + connections +
                '}';
    }
}