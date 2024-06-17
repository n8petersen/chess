package websocket.commands;

public class ResignCommand extends UserGameCommand {
    private final int gameId;

    public ResignCommand(String authToken, int gameId) {
        super(authToken);
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }
}
