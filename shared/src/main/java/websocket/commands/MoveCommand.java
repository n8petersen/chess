package websocket.commands;

import chess.ChessMove;

public class MoveCommand extends UserGameCommand {
    private final int gameId;
    private final ChessMove move;

    public MoveCommand(String authToken, int gameId, ChessMove move) {
        super(authToken);
        super.commandType = CommandType.MAKE_MOVE;
        this.gameId = gameId;
        this.move = move;
    }

    public int getGameId() {
        return gameId;
    }

    public ChessMove getMove() {
        return move;
    }
}
