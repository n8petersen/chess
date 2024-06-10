package clientutil;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import ui.GameList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

import java.util.Map;

public class ServerFacade {

    private final String hostname;
    private final Gson serializer = new Gson();

    public ServerFacade(String host, int port) {
        hostname = String.format("http://%s:%d", host, port);
    }

    public AuthData register(String username, String password, String email) throws Exception {
        var body = Map.of("username", username, "password", password, "email", email);
        return sendRequest("POST", "/user", body, null, AuthData.class);
    }

    public AuthData login(String username, String password) throws Exception {
        var body = Map.of("username", username, "password", password);
        return sendRequest("POST", "/session", body, null, AuthData.class);
    }

    public void logout(String authToken) throws Exception {
        sendRequest("DELETE", "/session", null, authToken, null);
    }

    public GameData createGame(String authToken, String gameName) throws Exception {
        var body = Map.of("gameName", gameName);
        return sendRequest("POST", "/game", body, authToken, GameData.class);
    }

    public GameList listGames(String authToken) throws Exception {
        return sendRequest("GET", "/game", null, authToken, GameList.class);
    }

    public GameData joinGame(String authToken, int gameID, ChessGame.TeamColor color) throws Exception {
        var body = Map.of("playerColor", color, "gameID", gameID);
        return sendRequest("PUT", "/game", body, authToken, GameData.class);
    }

    public void clear() throws Exception {
        sendRequest("DELETE", "/db", null, null, null);
    }

    private <T> T sendRequest(String httpMethod, String endpoint, Object body, String authToken, Class<T> classType) throws Exception {
        URL url = (new URI(hostname + endpoint)).toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(httpMethod);
        http.setDoOutput(true);
        if (authToken != null) {
            http.addRequestProperty("authorization", authToken);
        }
        if (body != null) {
            http.addRequestProperty("Accept", "application/json");
            var bodyJson = serializer.toJson(body);
            var outStream = http.getOutputStream();
            outStream.write(bodyJson.getBytes());
        }
        http.connect();
        InputStream respBody = http.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(respBody);
        if (classType != null) {
            return serializer.fromJson(inputStreamReader, classType);
        }
        return null;
    }
}
