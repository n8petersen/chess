package dataaccess;

import dataaccess.dao.IntAuthDAO;
import model.AuthData;

import java.sql.*;

public class SqlAuthDAO implements IntAuthDAO {

    public SqlAuthDAO() {
        configureDb();
    }

    public AuthData createAuth(String username) throws DataAccessException {
        AuthData newAuth = new AuthData(AuthData.createToken(), username);
        new SQLExecutor().updateQuery("INSERT INTO auth (username,authtoken) VALUES (?, ?);",
                username,
                newAuth.authToken());
        return newAuth;
    }

    public AuthData readAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM auth WHERE authtoken=?;";
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String user = rs.getString("username");
                        return new AuthData(authToken, user);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to modify database: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("Unable to modify database: null authToken");
        } else {
            new SQLExecutor().updateQuery("DELETE FROM auth WHERE authtoken=?;",
                    authToken);
        }
    }

    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE auth;";
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to modify database: %s", e.getMessage()));
        }
    }

    private final String[] createTable = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              id int NOT NULL AUTO_INCREMENT,
              username varchar(45) NOT NULL,
              authtoken varchar(100) NOT NULL,
              PRIMARY KEY (id),
              UNIQUE KEY authtoken_UNIQUE (authtoken),
              UNIQUE KEY id_UNIQUE (id)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private void configureDb() {
        new SQLExecutor().configureDb(createTable);
    }
}
