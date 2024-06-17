package websocket.messages;

public class ErrorMessage extends ServerMessage {

    public String message;

    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.message = errorMessage;
    }
}
