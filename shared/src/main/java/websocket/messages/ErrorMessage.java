package websocket.messages;

public class ErrorMessage extends ServerMessage {

    public String errorMessage;

    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }
}
