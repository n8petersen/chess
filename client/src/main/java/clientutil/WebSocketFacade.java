package clientutil;

import com.google.gson.Gson;
import model.GameData;
import ui.ChessClient;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    private final Session session;
    private final ChessClient client;

    public WebSocketFacade(String host, int port, ChessClient client) throws URISyntaxException, DeploymentException, IOException {
        this.client = client;

        var url = String.format("ws://%s:%d/ws", host, port);
        URI socketURI = new URI(url);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);

        this.session.addMessageHandler((MessageHandler.Whole<String>) s -> {
            var serializer = new Gson();

            try {
                ServerMessage serverMessage = serializer.fromJson(s, ServerMessage.class);
                switch (serverMessage.getServerMessageType()) {
                    case LOAD_GAME -> loadGame(serializer.fromJson(s, LoadMessage.class).game);
                    case NOTIFICATION -> notification(serializer.fromJson(s, NotificationMessage.class));
                    case ERROR -> error(serializer.fromJson(s, ErrorMessage.class));
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });
    }

    public void loadGame(GameData newGameData) {
        client.loadGame(newGameData);
    }

    private void notification(NotificationMessage notificationMessage) {
        client.notification(notificationMessage);
    }

    private void error(ErrorMessage errorMessage) {
        client.error(errorMessage);
    }

    public void sendCommand(UserGameCommand command) throws IOException {
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
