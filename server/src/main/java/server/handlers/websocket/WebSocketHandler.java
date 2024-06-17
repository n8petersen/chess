package server.handlers.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import model.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocket.messages.*;

@WebSocket
public class WebSocketHandler {
    private final DataAccess dataAccess;
    private final ConnectionManager connectionManager = new ConnectionManager();

    public WebSocketHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @OnWebSocketConnect
    public void onConnect(Session ignoredsession) {
    }

    @OnWebSocketClose
    public void onClose(Session session, int ignoredStatusCode, String ignoredReason) {
        connectionManager.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
            var command = new Gson().fromJson(message, UserGameCommand.class);

            var authData = dataAccess.authDAO().readAuth(command.getAuthString());
            var userData = dataAccess.userDAO().readUser(authData.username());
            var gameData = dataAccess.gameDAO().readGame(command.getGameID());

            var connection = connectionManager.add(userData.username(), new Connection(userData, session));
            connection.gameData = gameData;

            switch (command.getCommandType()) {
                case CONNECT -> connect(connection, gameData, userData);
                case MAKE_MOVE -> move(command, connection, gameData, userData);
                case LEAVE -> leave(connection, gameData, userData);
                case RESIGN -> resign(connection, gameData, userData);
            }

        } catch (Exception e) {
            Connection.sendError(session.getRemote(), e.getMessage());
        }
    }

    private void connect(Connection connection, GameData gameData, UserData userData) throws Exception {
        if (gameData != null) {
            connection.gameData = gameData;
            var loadMessage = new LoadMessage(gameData).toString();
            connection.send(loadMessage);

            String teamColor;
            if (userData.username().equalsIgnoreCase(gameData.whiteUsername())) {
                teamColor = "white";
            } else if (userData.username().equalsIgnoreCase(gameData.blackUsername())) {
                teamColor = "black";
            } else {
                teamColor = "observer";
            }

            String notificationMessage = (new NotificationMessage(String.format("%s joined game '%s' as %s", connection.userData.username(), gameData.gameName(), teamColor))).toString();
            connectionManager.broadcast(gameData.gameID(), connection.userData.username(), notificationMessage);
        } else {
            connection.sendError("couldn't find game");
        }
    }

    private void move(UserGameCommand command, Connection connection, GameData gameData, UserData userData) throws Exception {
        if (gameData != null) {
            if (gameData.state() == GameData.State.UNKNOWN) {
                var turnColor = gameData.game().getTeamTurn();
                var pieceColor = gameData.game().getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor();
                ChessGame.TeamColor playerColor = null;
                if (userData.username().equalsIgnoreCase(gameData.whiteUsername())) {
                    playerColor = ChessGame.TeamColor.WHITE;
                } else if (userData.username().equalsIgnoreCase(gameData.blackUsername())) {
                    playerColor = ChessGame.TeamColor.BLACK;
                }
                if (playerColor != null && playerColor.equals(pieceColor) && playerColor.equals(turnColor)) {
                    gameData.game().makeMove(command.getMove());
                    String notificationMessage = (new NotificationMessage(String.format("%s made move%s", userData.username(), command.getMove()))).toString();
                    connectionManager.broadcast(gameData.gameID(), connection.userData.username(), notificationMessage);
                    dataAccess.gameDAO().updateGame(gameData);
                    connection.gameData = gameData;
                    connectionManager.broadcast(gameData.gameID(), "", new LoadMessage(gameData).toString());
                } else {
                    connection.sendError("invalid move");
                }
                if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                    // set black to winner and announce
                    gameData = gameData.setState(GameData.State.BLACK);
                    dataAccess.gameDAO().updateGame(gameData);
                    connection.gameData = gameData;
                    String notificationMessage = (new NotificationMessage("Black won the game!")).toString();
                    connectionManager.broadcast(gameData.gameID(), "", notificationMessage);
//                    connectionManager.broadcast(gameData.gameID(), "", new LoadMessage(gameData).toString());
                } else if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                    // set white to winner and announce
                    gameData = gameData.setState(GameData.State.WHITE);
                    dataAccess.gameDAO().updateGame(gameData);
                    connection.gameData = gameData;
                    String notificationMessage = (new NotificationMessage("White won the game!")).toString();
                    connectionManager.broadcast(gameData.gameID(), "", notificationMessage);
//                    connectionManager.broadcast(gameData.gameID(), "", new LoadMessage(gameData).toString());
                }
            } else {
                connection.sendError("game is over");
            }
        } else {
            connection.sendError("couldn't find game");
        }
    }

    private void leave(Connection connection, GameData gameData, UserData userData) throws Exception {
        if (gameData != null) {
            if (userData.username().equalsIgnoreCase(gameData.whiteUsername())) {
                gameData = gameData.setWhite(null);
            } else if (userData.username().equalsIgnoreCase(gameData.blackUsername())) {
                gameData = gameData.setBlack(null);
            }
            dataAccess.gameDAO().updateGame(gameData);
            connectionManager.remove(connection.session);
            String notificationMessage = (new NotificationMessage(String.format("%s left the game", userData.username()))).toString();
            connectionManager.broadcast(gameData.gameID(), "", notificationMessage);
        } else {
            connection.sendError("couldn't find game");
        }
    }

    private void resign(Connection connection, GameData gameData, UserData userData) throws Exception {
        if (gameData != null && gameData.state() == GameData.State.UNKNOWN) {
            ChessGame.TeamColor playerColor = null;
            if (userData.username().equalsIgnoreCase(gameData.whiteUsername())) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (userData.username().equalsIgnoreCase(gameData.blackUsername())) {
                playerColor = ChessGame.TeamColor.BLACK;
            }
            if (playerColor != null) {
                var newState = (playerColor == ChessGame.TeamColor.WHITE ? GameData.State.BLACK : GameData.State.WHITE);
                gameData = gameData.setState(newState);
                dataAccess.gameDAO().updateGame(gameData);
                connection.gameData = gameData;

                String notificationMessage = (new NotificationMessage(String.format("%s resigned", userData.username()))).toString();
                connectionManager.broadcast(gameData.gameID(), "", notificationMessage);
            } else {
                connection.sendError("only players can resign");
            }
        } else {
            connection.sendError("couldn't find game");
        }
    }


}
