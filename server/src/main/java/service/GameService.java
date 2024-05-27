package service;

import dataaccess.DataAccessException;
import dataaccess.IntGameDAO;
import model.GameData;

import java.util.Collection;

public class GameService {

    private IntGameDAO gameDataAccess;

    private GameService() {}

    public int createGame(String gameName) {
        try {
            return gameDataAccess.createGame(gameName);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData getGame(int gameId) {
        try {
            return gameDataAccess.readGame(gameId);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<GameData> listGames() {
        try {
            return gameDataAccess.readAllGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateGame(GameData game) {
        try {
            gameDataAccess.updateGame(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDame(int gameId) {
        try {
            gameDataAccess.deleteGame(gameId);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearGames() {
        try {
            gameDataAccess.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
