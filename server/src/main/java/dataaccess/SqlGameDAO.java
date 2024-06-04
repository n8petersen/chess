package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
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
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE `chess`.`game`;";
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to add user to database: %s", e.getMessage()));
        }
    }

    private final String[] createTable = {
            """
            CREATE TABLE IF NOT EXISTS `game` (
            `id` int NOT NULL AUTO_INCREMENT,
            `gameName` varchar(45) DEFAULT NULL,
            `whiteUsername` varchar(45) DEFAULT NULL,
            `blackUsername` varchar(45) DEFAULT NULL,
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
