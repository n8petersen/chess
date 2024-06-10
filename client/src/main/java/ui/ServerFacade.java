package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.io.IOException;
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
        AuthData auth = null;
        URL url = (new URI(hostname + "/user")).toURL();
        var req = Map.of("username", username, "password", password, "email", email);
        var reqJson = serializer.toJson(req);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Accept", "application/json");
        var outStream = http.getOutputStream();
        outStream.write(reqJson.getBytes());
        http.connect();
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            auth = serializer.fromJson(inputStreamReader, AuthData.class);
        }
        return auth;
    }

    public AuthData login(String username, String password) throws Exception {
        AuthData auth = null;
        URL url = (new URI(hostname + "/session")).toURL();
        var req = Map.of("username", username, "password", password);
        var reqJson = serializer.toJson(req);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Accept", "application/json");
        var outStream = http.getOutputStream();
        outStream.write(reqJson.getBytes());
        http.connect();
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            auth = serializer.fromJson(inputStreamReader, AuthData.class);
        }
        return auth;
    }

    public void logout(String authToken) throws Exception {
        URL url = (new URI(hostname + "/session")).toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("DELETE");
        http.addRequestProperty("authorization", authToken);
        http.connect();
        InputStream respBody = http.getInputStream();
    }

    public GameData createGame(String authToken, String gameName) {


        return null;
    }

    public GameData[] listGames(String authToken) {


        return null;
    }

    public GameData joinGame(String authToken, int gameId, ChessGame.TeamColor color) {


        return null;
    }

    public void clear() {

    }
}
