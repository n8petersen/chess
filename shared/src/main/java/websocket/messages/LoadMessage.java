package websocket.messages;

import model.GameData;

public class LoadMessage extends ServerMessage {

    public GameData game;

    public LoadMessage(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
}
