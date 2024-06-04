package dataaccess;

import chess.ChessGame;
import model.GameData;
import com.google.gson.Gson;

import java.sql.*;
import java.util.Collection;
import java.util.List;

public class SqlGameDAO implements IntGameDAO {

    private final Gson serializer = new Gson();

    public SqlGameDAO() {
        configureDb();
    }

    public GameData createGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        String gameJson = serializer.toJson(game);
        int id = new SQLExecutor().updateQuery("INSERT INTO `chess`.`game` (`gameName`, `whiteUsername`, `blackUsername`, `gameData`) VALUES (?, ?, ?, ?)",
                gameName,
                null,
                null,
                gameJson);
        return new GameData(id, gameName, null, null, game);
    }

    public GameData readGame(int gameId) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM `chess`.`game` WHERE `id` = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, gameId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String gameName = rs.getString("gameName");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameJson = rs.getString("GameData");
                        ChessGame game = serializer.fromJson(gameJson, ChessGame.class);
                        return new GameData(gameId, gameName, whiteUsername, blackUsername, game);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Collection<GameData> readAllGames() throws DataAccessException {
        return List.of();
    }

    public void updateGame(GameData game) throws DataAccessException {

    }

    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE `chess`.`game`;";
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to modify database: %s", e.getMessage()));
        }
    }

    private final String[] createTable = {
            """
            CREATE TABLE IF NOT EXISTS `game` (
            `id` int NOT NULL AUTO_INCREMENT,
            `gameName` varchar(45) DEFAULT NULL,
            `whiteUsername` varchar(45) DEFAULT NULL,
            `blackUsername` varchar(45) DEFAULT NULL,
            `gameData` json DEFAULT NULL,
            PRIMARY KEY (`id`),
            UNIQUE KEY `id_UNIQUE` (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private void configureDb() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (java.sql.Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createTable) {
                try (java.sql.PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException e) {
            try {
                throw new BadRequestException(String.format("Unable to configure database: %s", e.getMessage()));
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
