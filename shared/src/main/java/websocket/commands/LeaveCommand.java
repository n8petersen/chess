package websocket.commands;

public class LeaveCommand extends UserGameCommand {
    private final int gameId;

    public LeaveCommand(String authToken, int gameId) {
        super(authToken);
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }
}
