package ui;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

public class ServerFacade {

    public AuthData register(String username, String password, String email) {


        return null;
    }

    public AuthData login(String username, String password) {


        return null;
    }

    public void logout(String authToken) {

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
