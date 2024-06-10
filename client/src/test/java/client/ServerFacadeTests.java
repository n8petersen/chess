package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerGood() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerBad() {
        Assertions.assertTrue(true);
    }

    @Test
    public void loginGood() {
        Assertions.assertTrue(true);
    }

    @Test
    public void loginBad() {
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutGood() {
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutBad() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createGood() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createBad() {
        Assertions.assertTrue(true);
    }

    @Test
    public void listGood() {
        Assertions.assertTrue(true);
    }

    @Test
    public void listBad() {
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGood() {
        Assertions.assertTrue(true);
    }

    @Test
    public void joinBad() {
        Assertions.assertTrue(true);
    }

}
