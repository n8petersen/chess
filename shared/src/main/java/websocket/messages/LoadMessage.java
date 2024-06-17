package websocket.messages;

public class LoadMessage extends ServerMessage {

    public String message;

    public LoadMessage(String game) {
        super(ServerMessageType.LOAD_GAME);
        this.message = game;
    }
}
