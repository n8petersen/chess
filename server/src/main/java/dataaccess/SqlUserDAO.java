package dataaccess;

import dataaccess.dao.IntUserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class SqlUserDAO implements IntUserDAO {

    public SqlUserDAO() {
        configureDb();
    }

    public UserData createUser(UserData user) throws DataAccessException {
        new SQLExecutor().updateQuery("INSERT INTO user (username, email, password) VALUES (?, ?, ?);",
                user.username(),
                user.email(),
                BCrypt.hashpw(user.password(), BCrypt.gensalt()));
        return new UserData(user.username(), BCrypt.hashpw(user.password(), BCrypt.gensalt()), user.email());
    }

    public UserData readUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM user WHERE username=?;";
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String password = rs.getString("password");
                        String email = rs.getString("email");
                        return new UserData(username, password, email);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to modify database: %s", e.getMessage()));
        }
        return null;
    }

    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE user;";
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to add user to database: %s", e.getMessage()));
        }
    }

    private final String[] createTable = {
            """
            CREATE TABLE IF NOT EXISTS user (
              id int NOT NULL AUTO_INCREMENT,
              username varchar(45) NOT NULL,
              email varchar(45) DEFAULT NULL,
              password varchar(100) DEFAULT NULL,
              PRIMARY KEY (id,username),
              UNIQUE KEY username_UNIQUE (username),
              UNIQUE KEY id_UNIQUE (id)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private void configureDb() {
        new SQLExecutor().configureDb(createTable);
    }
}
