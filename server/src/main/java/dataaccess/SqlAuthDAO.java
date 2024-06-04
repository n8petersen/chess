package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class SqlAuthDAO implements IntAuthDAO {

    public SqlAuthDAO() {
        configureDb();
    }

    public AuthData createAuth(String username) throws DataAccessException {
        AuthData newAuth = new AuthData(null, null);
        return newAuth;
    }

    public AuthData readAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE `chess`.`auth`;";
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to add user to database: %s", e.getMessage()));
        }
    }

    private final String[] createTable = {
            """
            CREATE TABLE IF NOT EXISTS `auth` (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(45) NOT NULL,
              `authtoken` varchar(100) NOT NULL,
              PRIMARY KEY (`id`),
              UNIQUE KEY `authtoken_UNIQUE` (`authtoken`),
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
