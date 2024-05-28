package service;

import dataaccess.*;
import model.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class GameService {

    private final IntGameDAO gameDataAccess;
    private final IntAuthDAO authDataAccess;

    public GameService(IntGameDAO gameDataAccess, IntAuthDAO authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    /* Public functions */
    public void clearGames() throws DataAccessException {
        gameDataAccess.clear();
    }

    public GameData createNewGame(GameData game, String authToken) throws DataAccessException, BadRequestException, UnauthorizedException {
        AuthData auth = authDataAccess.readAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("unauthorized");
        } else if (game.gameName().isEmpty()) {
            throw new BadRequestException("bad request");
        } else {
            return createGame(game.gameName());
        }
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData auth = authDataAccess.readAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("unauthorized");
        } else {
            return getAllGames();
        }
    }

    public void joinGame(String authToken, int gameId, String chosenColor) throws DataAccessException, UnauthorizedException, UserTakenException, BadRequestException {
        AuthData auth = authDataAccess.readAuth(authToken);
        GameData game = getGame(gameId);
        if (auth == null) {
            throw new UnauthorizedException("unauthorized");
        } else if (game != null ) {
            if (Objects.equals(chosenColor, "WHITE") && game.whiteUsername() == null) {
                updateGame(new GameData(gameId, auth.username(), null, game.gameName(), game.game()));
            } else if (Objects.equals(chosenColor, "BLACK") && game.blackUsername() == null) {
                updateGame(new GameData(gameId, null, auth.username(), game.gameName(), game.game()));
            } else {
                throw new UserTakenException("already taken");
        }
        // At this point if we haven't been able to add a player or throw another exception, we need to throw a general bad request.
        throw new BadRequestException("bad request");
        }

        // TODO: Check game for players of current color to see if chosenColor is available in game.
        //   throw a UserTakenException if it is not available. Else, change that color's username to auth.username().

    }

    /* Helper Functions */
    private GameData createGame(String gameName) throws DataAccessException {
        return gameDataAccess.createGame(gameName);
    }

    private GameData getGame(int gameId) throws DataAccessException {
        return gameDataAccess.readGame(gameId);
    }

    private Collection<GameData> getAllGames() throws DataAccessException {
        return gameDataAccess.readAllGames();
    }

    private void updateGame(GameData game) throws DataAccessException {
        gameDataAccess.updateGame(game);
    }

    private void deleteDame(int gameId) throws DataAccessException {
        gameDataAccess.deleteGame(gameId);
    }

}
