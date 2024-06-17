package websocket.commands;

public class LeaveCommand extends UserGameCommand {
    private final int gameId;

    public LeaveCommand(String authToken, int gameId) {
        super(authToken);
        super.commandType = CommandType.LEAVE;
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }
}
