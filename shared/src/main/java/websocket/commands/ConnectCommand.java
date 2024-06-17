package websocket.commands;

import chess.ChessGame;

public class ConnectCommand extends UserGameCommand {

    public ConnectCommand(String authToken, String username, int gameId, boolean observer) {
        super(authToken);
        super.commandType = CommandType.CONNECT;
        super.gameId = gameId;
        super.playerColor = null;
        super.observer = observer;
        super.username = username;
    }

    public ConnectCommand(String authToken, String username, int gameId, ChessGame.TeamColor playerColor) {
        super(authToken);
        super.gameId = gameId;
        super.playerColor = playerColor;
        super.observer = false;
        super.username = username;
    }
}
