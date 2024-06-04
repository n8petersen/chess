package dataaccess;

import java.sql.*;

import static java.sql.Types.NULL;

public class SQLExecutor {

    public int updateQuery(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    switch (param) {
                        case String p -> {
                            if (p.equals("null")) {
                                ps.setNull(i + 1, NULL);
                            } else {
                                ps.setString(i + 1, p);
                            }
                        }
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to update database: %s", e.getMessage()));
        }
    }

    public void configureDb(String[] createStatement) {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (java.sql.Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatement) {
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
