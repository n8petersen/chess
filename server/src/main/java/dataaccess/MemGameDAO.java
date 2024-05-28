package dataaccess;

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
        GameData newGameData = new GameData(newGameID, null, null, gameName, new ChessGame());
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

//    public void deleteGame(int gameId) {
//        games.remove(gameId);
//    }

    public void clear() throws DataAccessException {
        games.clear();
        newGameID = 100;
    }
}
