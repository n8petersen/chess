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
        } else if (game != null) {
            if (Objects.equals(chosenColor, "WHITE") && (game.whiteUsername() == null || game.whiteUsername().isEmpty())) {
                updateGame(new GameData(gameId, auth.username(), game.blackUsername(), game.gameName(), game.game()));
            } else if (Objects.equals(chosenColor, "BLACK") && (game.blackUsername() == null || game.blackUsername().isEmpty())) {
                updateGame(new GameData(gameId, game.whiteUsername(), auth.username(), game.gameName(), game.game()));
            } else {
                throw new UserTakenException("already taken");
            }
        } else {
            throw new BadRequestException("bad request");
        }
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
