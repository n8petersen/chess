package websocket.commands;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    private final String authToken;
    protected String username;
    protected CommandType commandType;
    protected boolean observer;
    protected ChessGame.TeamColor playerColor;
    protected int gameId;


    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public boolean isObserver() {
        return observer;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public int getGameId() {
        return gameId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
