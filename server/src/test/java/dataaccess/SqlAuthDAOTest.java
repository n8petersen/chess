package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

class SqlAuthDAOTest {

    private IntAuthDAO authDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        (authDao = new SqlAuthDAO()).clear();
    }

    @Test
    void createNewAuth() throws DataAccessException {
        AuthData testAuth = new AuthData(AuthData.createToken(), "user");
        AuthData insertAuth = authDao.createAuth("user");
        assertNotEquals(testAuth.authToken(), insertAuth.authToken());
        assertNotNull(insertAuth.authToken());
        assertNotNull(insertAuth);
    }

    @Test
    void createAuthNullUsername() {
        assertThrows(DataAccessException.class,
                () -> authDao.createAuth(null)
        );
    }

    @Test
    void readAuth() throws DataAccessException {
        AuthData insertAuth = authDao.createAuth("user");
        AuthData readAuth = authDao.readAuth(insertAuth.authToken());
        assertEquals(readAuth, insertAuth);
    }

    @Test
    void readAuthNullToken() throws DataAccessException {
        AuthData insertAuth = authDao.createAuth("user");
        AuthData readAuth = authDao.readAuth(null);
        assertNotEquals(readAuth, insertAuth);
        assertNull(readAuth);
    }

    @Test
    void deleteAuth() throws DataAccessException {
        AuthData insertAuth = authDao.createAuth("user");
        authDao.deleteAuth(insertAuth.authToken());
        AuthData readAuth = authDao.readAuth(null);
        assertNull(readAuth);
        assertNotEquals(readAuth, insertAuth);
    }

    @Test
    void deleteAuthNullToken() throws DataAccessException {
        assertThrows(DataAccessException.class,
                () -> {
                    authDao.createAuth("user");
                    authDao.deleteAuth(null);
                }
        );
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