package websocket.commands;

import chess.ChessGame;

public class ConnectCommand extends UserGameCommand {
    private final int gameId;
    private final ChessGame.TeamColor playerColor;
    private final boolean observer;

    public ConnectCommand(String authToken, int gameId, boolean observer) {
        super(authToken);
        super.commandType = CommandType.CONNECT;
        this.gameId = gameId;
        this.playerColor = null;
        this.observer = observer;
    }

    public ConnectCommand(String authToken, int gameId, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameId = gameId;
        this.playerColor = playerColor;
        this.observer = false;
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
}
