package dataaccess.dao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface IntGameDAO {

    /**
     * Create a new game instance
     *
     * @param gameName Name of Game to create
     * @return ID of game created
     * @throws DataAccessException for DB access violations
     */
    GameData createGame(String gameName) throws DataAccessException;

    /**
     * Find a game given a game ID
     *
     * @param gameId ID of game to retrieve
     * @return GameData of the game we retrieved
     * @throws DataAccessException for DB access violations
     */
    GameData readGame(int gameId) throws DataAccessException;

    /**
     * Find all available games
     *
     * @return Collection of games
     * @throws DataAccessException for DB access violations
     */
    Collection<GameData> readAllGames() throws DataAccessException;

    /**
     * Update a current game
     *
     * @param game - GameData to update with
     * @throws DataAccessException for DB access violations
     */
    void updateGame(GameData game) throws DataAccessException;

    /**
     * Delete all games
     *
     * @throws DataAccessException for DB access violations
     */
    void clear() throws DataAccessException;
}
