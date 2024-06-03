package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SqlGameDAO implements IntGameDAO {

    public SqlGameDAO() {
        configureDb();
    }

    public GameData createGame(String gameName) throws DataAccessException {
        return null;
    }

    public GameData readGame(int gameId) throws DataAccessException {
        return null;
    }

    public Collection<GameData> readAllGames() throws DataAccessException {
        return List.of();
    }

    public void updateGame(GameData game) throws DataAccessException {

    }

    public void clear() throws DataAccessException {

    }

    private void configureDb() {

    }
}
