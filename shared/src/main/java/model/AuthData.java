package model;

import java.util.UUID;

public record AuthData(String authToken, String username) {

        public static String createToken() { return UUID.randomUUID().toString(); }

}
