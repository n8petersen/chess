package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;
    private final String authToken;

    private int gameID;
    private ChessMove move;

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    public UserGameCommand(String authToken, CommandType commandType, int gameId) {
        this.authToken = authToken;
        this.commandType = commandType;
        this.gameID = gameId;
    }

    public UserGameCommand(String authToken, CommandType commandType, int gameId, ChessMove move) {
        this.authToken = authToken;
        this.commandType = commandType;
        this.gameID = gameId;
        this.move = move;
    }


    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
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
