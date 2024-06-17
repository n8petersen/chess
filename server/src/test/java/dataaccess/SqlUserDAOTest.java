package dataaccess;

import dataaccess.dao.IntUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;


class SqlUserDAOTest {

    private IntUserDAO userDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        (userDao = new SqlUserDAO()).clear();
    }

    @Test
    void createNewUser() throws DataAccessException {
        UserData testUser = new UserData("user", "pass", "email");
        UserData insertUser = userDao.createUser(testUser);
        assertTrue(BCrypt.checkpw(testUser.password(), insertUser.password()));
    }

    @Test
    void createUserTaken() {
        assertThrows(DataAccessException.class,
                () -> {
                    userDao.createUser(new UserData("user", "pass", "email"));
                    userDao.createUser(new UserData("user", "pass", "email"));
                }
        );
    }

    @Test
    void readUser() throws DataAccessException {
        UserData testUser = new UserData("user", "pass", "email");
        userDao.createUser(testUser);
        UserData readUser = userDao.readUser("user");
        assertTrue(BCrypt.checkpw(testUser.password(), readUser.password()));
    }

    @Test
    void readUserNullUsername() throws DataAccessException {
        UserData userData = userDao.readUser(null);
        assertNull(userData);
    }

    @Test
    void clear() throws DataAccessException {
        userDao.createUser(new UserData("user1", "pass", "user1"));
        userDao.createUser(new UserData("user2", "pass", "user2"));
        userDao.createUser(new UserData("user3", "pass", "user3"));
        userDao.clear();
        assertNull(userDao.readUser("user1"));
        assertNull(userDao.readUser("user2"));
        assertNull(userDao.readUser("user3"));
    }
}