package dataaccess.dao;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemGameDAO implements IntGameDAO {

    int newGameID = 100;
    private final Map<Integer, GameData> games = new HashMap<>();

    public MemGameDAO() {
    }

    public GameData createGame(String gameName) {
        newGameID++;
        GameData newGameData = new GameData(newGameID, null, null, gameName, new ChessGame(), GameData.State.UNKNOWN);
        games.put(newGameID, newGameData);
        return newGameData;
    }

    public GameData readGame(int gameId) {
        return games.get(gameId);
    }

    public Collection<GameData> readAllGames() {
        return games.values();
    }

    public void updateGame(GameData game) {
        games.put(game.gameID(), game);
    }

    public void clear() {
        games.clear();
        newGameID = 100;
    }
}
