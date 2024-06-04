package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class SqlUserDAOTest {

    private IntUserDAO userDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        (userDao = new SqlUserDAO()).clear();
    }

    @Test
    void createNewUser() {
    }

    @Test
    void createUserTaken() {
    }

    @Test
    void readUser() {
    }

    @Test
    void readUserNullUsername() {
    }

    @Test
    void clear() {
    }
}