package service;

import dataaccess.DataAccessException;
import dataaccess.IntGameDAO;
import model.GameData;
import java.util.Collection;

public class GameService {

    private final IntGameDAO gameDataAccess;

    public GameService(IntGameDAO gameDataAccess) {
        this.gameDataAccess = gameDataAccess;
    }

    public int createGame(String gameName) throws DataAccessException {
        return gameDataAccess.createGame(gameName);
    }

    public GameData getGame(int gameId) throws DataAccessException {
        return gameDataAccess.readGame(gameId);
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return gameDataAccess.readAllGames();
    }

    public void updateGame(GameData game) throws DataAccessException {
        gameDataAccess.updateGame(game);
    }

    public void deleteDame(int gameId) throws DataAccessException {
        gameDataAccess.deleteGame(gameId);
    }

    public void clearGames() throws DataAccessException {
        gameDataAccess.clear();
    }
}
