package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;

public class SqlUserDAO implements IntUserDAO {

    public SqlUserDAO() {
        configureDb();
    }

    public void createUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO `chess`.`user` (`username`, `email`, `password`) VALUES (?, ?, ?)";
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.username());
                ps.setString(2, user.email());
                ps.setString(3, user.password());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to add user to database: %s", e.getMessage()));
        }
    }

    public UserData readUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM `chess`.`user` WHERE `username` = ?";
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String user = rs.getString("username");
                        String email = rs.getString("email");
                        String password = rs.getString("password");
                        return new UserData(user, email, password);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE `chess`.`user`;";
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to add user to database: %s", e.getMessage()));
        }
    }

    private final String[] createTable = {
            """
            CREATE TABLE IF NOT EXISTS `user` (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(45) NOT NULL,
              `email` varchar(45) DEFAULT NULL,
              `password` varchar(100) DEFAULT NULL,
              PRIMARY KEY (`id`,`username`),
              UNIQUE KEY `username_UNIQUE` (`username`),
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
