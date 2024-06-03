package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class SqlUserDAO implements IntUserDAO {

    public SqlUserDAO() {
        configureDb();
    }

    public void createUser(UserData user) throws DataAccessException {
        String statement = "INSERT INTO `chess`.`user` (`username`, `email`, `password`) VALUES (?, ?, ?)";
        try {
            executeSql(statement, user.username(), user.email(), user.password());
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    public UserData readUser(String username) throws DataAccessException {
        return null;
    }

    public void clear() throws DataAccessException {

    }

    private int executeSql(String statement, Object... params) throws DataAccessException, BadRequestException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setInt(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new BadRequestException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    private final String[] createTable = {
            """
            CREATE TABLE IF NOT EXISTS `user` (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(45) NOT NULL,
              `email` varchar(45) DEFAULT NULL,
              `password` varchar(45) DEFAULT NULL,
              PRIMARY KEY (`id`,`username`),
              UNIQUE KEY `username_UNIQUE` (`username`),
              UNIQUE KEY `id_UNIQUE` (`id`),
              UNIQUE KEY `email_UNIQUE` (`email`)
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
