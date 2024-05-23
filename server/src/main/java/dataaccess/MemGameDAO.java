package dataaccess;

import model.GameData;

import java.util.Collection;

public class MemGameDAO {

    public MemGameDAO() {
    }

    public int createGame(GameData game) throws DataAccessException {
        return 0;
    }

    public GameData readGame(int GameID) throws DataAccessException {
        return null;
    }

    public Collection<GameData> readAllGames() throws DataAccessException {
        return null;
    }

    public void updateGame(GameData game) throws DataAccessException {

    }

    public void deleteGame(int gameId) throws DataAccessException {

    }

    public void clear() throws DataAccessException {

    }
}
