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
        String newAuthToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(newAuthToken, username);
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO `chess`.`auth` (`username`,`authtoken`) VALUES (?, ?)";
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                ps.setString(2, newAuthToken);
                ps.executeUpdate();
                return newAuth;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to add user to database: %s", e.getMessage()));
        }
    }

    public AuthData readAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM `chess`.`auth` WHERE `authtoken` = ?";
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String user = rs.getString("username");
                        return new AuthData(authToken, user);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM `chess`.`auth` WHERE `authtoken` = ?";
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to add user to database: %s", e.getMessage()));
        }
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
