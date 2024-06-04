package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlAuthDAOTest {

    private IntAuthDAO authDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        (authDao = new SqlAuthDAO()).clear();
    }

    @Test
    void createNewAuth() {
    }

    @Test
    void createAuthNullUsername() {
    }

    @Test
    void readAuth() {
    }

    @Test
    void readAuthNullToken() {
    }

    @Test
    void deleteAuth() {
    }

    @Test
    void deleteAuthNullToken() {
    }

    @Test
    void clear() throws DataAccessException {
        AuthData auth1 = authDao.createAuth("user1");
        AuthData auth2 = authDao.createAuth("user2");
        AuthData auth3 = authDao.createAuth("user3");
        authDao.clear();
        assertNull(authDao.readAuth(auth1.authToken()));
        assertNull(authDao.readAuth(auth2.authToken()));
        assertNull(authDao.readAuth(auth3.authToken()));
    }
}