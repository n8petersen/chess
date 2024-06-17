package dataaccess;

import chess.ChessGame;
import dataaccess.dao.IntGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class SqlGameDAOTest {

    private IntGameDAO gameDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        (gameDao = new SqlGameDAO()).clear();
    }

    @Test
    void createGameGood() throws DataAccessException {
        GameData testGame = new GameData(1, null, null, "testName", new ChessGame(), GameData.State.UNKNOWN);
        GameData insertGame = gameDao.createGame("testName");
        assertEquals(testGame, insertGame);
        assertNotEquals(2, testGame.gameID());
    }

    @Test
    void createGameNullName() {
        assertThrows(DataAccessException.class,
                () -> gameDao.createGame(null)
        );
    }

    @Test
    void readGameGood() throws DataAccessException {
        GameData insertGame = gameDao.createGame("testName");
        GameData readGame = gameDao.readGame(insertGame.gameID());
        assertEquals(readGame, insertGame);
    }

    @Test
    void readGameWrongID() throws DataAccessException {
        GameData insertGame = gameDao.createGame("testName");
        GameData readGame = gameDao.readGame(2);
        assertNotEquals(readGame, insertGame);
        assertNull(readGame);
    }

    @Test
    void readAllGamesAfterInserts() throws DataAccessException {
        GameData insertGame1 = gameDao.createGame("one");
        GameData insertGame2 = gameDao.createGame("two");
        GameData insertGame3 = gameDao.createGame("three");
        Collection<GameData> readGames = gameDao.readAllGames();
        assertTrue(readGames.contains(insertGame1));
        assertTrue(readGames.contains(insertGame2));
        assertTrue(readGames.contains(insertGame3));
    }

    @Test
    void readAllGamesAfterBadInsert() throws DataAccessException {
        GameData insertGame1 = gameDao.createGame("one");
        GameData insertGame2 = gameDao.createGame("two");
        GameData insertGame3 = null;
        Collection<GameData> readGames = gameDao.readAllGames();
        assertTrue(readGames.contains(insertGame1));
        assertTrue(readGames.contains(insertGame2));
        assertFalse(readGames.contains(insertGame3));
    }

    @Test
    void readAllGamesEmpty() throws DataAccessException {
        Collection<GameData> readGames = gameDao.readAllGames();
        assertTrue(readGames.isEmpty());
    }

    @Test
    void updateGameGood() throws DataAccessException {
        GameData insertNewGame = gameDao.createGame("testName");
        GameData updateGameData = new GameData(1, "white", "black", "testName", new ChessGame(), GameData.State.UNKNOWN);
        gameDao.updateGame(updateGameData);
        GameData getGame = gameDao.readGame(1);
        assertNotEquals(updateGameData, insertNewGame);
        assertEquals(updateGameData, getGame);
    }

    @Test
    void updateGameNull() {
        assertThrows(DataAccessException.class,
                () -> {
                    gameDao.createGame("testName");
                    gameDao.updateGame(new GameData(1, "white", "black", "testName", null, GameData.State.UNKNOWN));
                }
        );
    }

    @Test
    void clear() throws DataAccessException {
        GameData insert1 = gameDao.createGame("game1");
        GameData insert2 = gameDao.createGame("game2");
        GameData insert3 = gameDao.createGame("game3");
        gameDao.clear();
        Collection<GameData> readGames = gameDao.readAllGames();
        assertFalse(readGames.contains(insert1));
        assertFalse(readGames.contains(insert2));
        assertFalse(readGames.contains(insert3));
        assertTrue(readGames.isEmpty());
    }
}