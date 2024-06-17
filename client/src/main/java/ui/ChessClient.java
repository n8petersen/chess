package ui;

import chess.*;
import clientutil.ServerFacade;
import clientutil.State;
import clientutil.WebSocketFacade;
import model.GameData;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static clientutil.State.*;

public class ChessClient {

    private final ServerFacade server;
    private final WebSocketFacade webSocket;
    private State state = LOGGED_OUT;
    private String username;
    private String authToken;
    private GameData gameData;
    private GameData[] gameList;
    private final Draw draw = new Draw();

    public ChessClient(String host, int port) throws DeploymentException, URISyntaxException, IOException {
        server = new ServerFacade(host, port);
        webSocket = new WebSocketFacade(host, port, this);
    }

    public void writePrompt() {
        if (state == LOGGED_OUT) {
            System.out.print("[LOGGED OUT] >> ");
        } else {
            System.out.print("[" + username + "] >> ");
        }
    }

    public String readInput(String input) throws Exception {
        var result = "Couldn't process command: " + input + ". Try running 'Help'";
        input = input.toLowerCase();
        var params = input.split(" ");

        if (state == LOGGED_OUT) {
            return switch (params[0]) {
                case "help" -> help();
                case "quit" -> quit();
                case "register" -> register(params);
                case "login" -> login(params);
                default -> result;
            };
        } else if (state == LOGGED_IN) {
            return switch (params[0]) {
                case "help" -> help();
                case "quit" -> quit();
                case "logout" -> logout();
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "create" -> create(params);
                case "draw" -> draw(params);
                default -> result;
            };
        } else if (state == WHITE || state == BLACK) {
            return switch (params[0]) {
                case "help" -> help();
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                default -> result;
            };
        } else if (state == OBSERVER) {
            return switch (params[0]) {
                case "help" -> help();
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "highlight" -> highlight(params);
                default -> result;
            };
        }
        return result;
    }

    private String help() {
        StringBuilder result = new StringBuilder();
        switch (state) {
            case LOGGED_IN:
                result.append(SET_TEXT_COLOR_BLUE + "list" + RESET_TEXT_COLOR + " - get existing games" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]" + RESET_TEXT_COLOR + " - join a game (as color)" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "observe <ID>" + RESET_TEXT_COLOR + " - observe a game" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "create <NAME>" + RESET_TEXT_COLOR + " - create new game" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "logout" + RESET_TEXT_COLOR + " - logout of account" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " - close program" + "\n");
                break;
            case WHITE, BLACK:
                result.append(SET_TEXT_COLOR_BLUE + "redraw" + RESET_TEXT_COLOR + " - redraw current board" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "leave" + RESET_TEXT_COLOR + " - leave current game" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "move <piece_start> <piece_end>" + RESET_TEXT_COLOR + " - make move in game" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "resign" + RESET_TEXT_COLOR + " - resign from current game" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "highlight <piece>" + RESET_TEXT_COLOR + " - highlight moves for given piece" + "\n");
                break;
            case OBSERVER:
                result.append(SET_TEXT_COLOR_BLUE + "redraw" + RESET_TEXT_COLOR + " - redraw current board" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "leave" + RESET_TEXT_COLOR + " - leave current game" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "highlight <piece>" + RESET_TEXT_COLOR + " - highlight moves for given piece" + "\n");
                break;
            case LOGGED_OUT:
                result.append(SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR + " - create new account" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR + " - login to existing account" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " - close program" + "\n");
                break;
            default:
                result.append(SET_TEXT_COLOR_BLUE + "logout" + RESET_TEXT_COLOR + " - logout of account" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " - close program" + "\n");
                break;
        }
        result.append(SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " - get possible commands");
        return result.toString();
    }

    private String register(String[] param) throws Exception {
        String result = "Couldn't register. Try again";
        try {
            if (param.length == 4) {
                var resp = server.register(param[1], param[2], param[3]);
                username = param[1];
                authToken = resp.authToken();
                state = LOGGED_IN;
                result = "Created and logged in as " + param[1];
            }
        } catch (IOException e) {
            result = "Couldn't register. Try different username.";
        }
        return result;
    }

    private String login(String[] param) throws Exception {
        String result = "Couldn't login. Try again";
        try {
            if (param.length == 3) {
                var resp = server.login(param[1], param[2]);
                username = param[1];
                authToken = resp.authToken();
                state = LOGGED_IN;
                result = "Logged in as " + param[1];
            }
        } catch (IOException e) {
            result = "Couldn't login, check credentials or register new user";
        }
        return result;
    }

    private String logout() throws Exception {
        server.logout(authToken);
        state = LOGGED_OUT;
        username = null;
        authToken = null;
        return "Logged out";
    }

    private String list() throws Exception {
        StringBuilder result = new StringBuilder();
        var games = server.listGames(authToken);
        gameList = games.games();
        for (int i = 0; i < gameList.length; i++) {
            var game = gameList[i];
            String whiteUser = (game.whiteUsername() == null ? "________" : game.whiteUsername());
            String blackUser = (game.blackUsername() == null ? "________" : game.blackUsername());
            result.append(String.format("%d - %s - W:%s B:%s\n", i + 1, game.gameName(), whiteUser, blackUser));
        }
        return result.toString();
    }

    private String join(String[] param) throws Exception {
        String result = "Couldn't join game";
        try {
            if (param.length == 3 && (param[2].equalsIgnoreCase("WHITE") || param[2].equalsIgnoreCase("BLACK"))) {
                int gameId = Integer.parseInt(param[1]);
                list();
                if (gameId <= gameList.length) {
                    gameId = gameList[gameId - 1].gameID();
                    var color = ChessGame.TeamColor.valueOf(param[2].toUpperCase());
                    gameData = server.joinGame(authToken, gameId, color);
                    webSocket.sendCommand(new UserGameCommand(authToken, UserGameCommand.CommandType.CONNECT, gameId));
                    state = (color == ChessGame.TeamColor.WHITE ? WHITE : BLACK);
                    result = "Joined game " + gameData.gameID() + " as " + color;
                    draw.drawBoard(gameData, state == WHITE);
                }
            }
        } catch (IOException e) {
            result = "Couldn't join game, try different color or game";
        } catch (NumberFormatException e) {
            result = "Please provide a valid game ID";
        }
        return result;
    }

    private String observe(String[] param) throws Exception {
        String result = "Couldn't observe game";
        try {
            if (param.length == 2) {
                list();
                int gameId = Integer.parseInt(param[1]);
                if (gameId <= gameList.length) {
                    gameData = gameList[gameId - 1];
                    state = OBSERVER;
                    result = "Joined game " + gameId + " as OBSERVER";
                    draw.drawBoard(gameData, true);
                    draw.drawBoard(gameData, false);
                    webSocket.sendCommand(new UserGameCommand(authToken, UserGameCommand.CommandType.CONNECT, gameId));
                }
            }
        } catch (IOException e) {
            return result;
        } catch (NumberFormatException e) {
            result = "GameID expected number";
        }
        return result;
    }

    private String create(String[] param) throws Exception {
        String result = "Couldn't create game";
        try {
            if (param.length == 2) {
                gameData = server.createGame(authToken, param[1]);
                result = "Created game " + gameData.gameID();
            }
        } catch (IOException e) {
            return result;
        }
        return result;
    }

    private String quit() throws Exception {
        if (state != LOGGED_OUT) {
            logout();
        }
        return "quit";
    }

    private String draw(String[] param) throws Exception {
        String result = "Couldn't draw game";
        try {
            if (param.length == 2) {
                list();
                if (gameList.length > 0) {
                    gameData = gameList[0];
                    draw.drawBoard(gameData, param[1].equalsIgnoreCase("white"));
                    result = "";
                }
            }
        } catch (IOException e) {
            return result;
        }
        return result;
    }

    private String redraw() throws Exception {
        String result = "Couldn't draw game";
        try {
            list();
            if (gameData != null) {
                if (state == OBSERVER) {
                    draw.drawBoard(gameData, true);
                    draw.drawBoard(gameData, false);
                } else {
                    draw.drawBoard(gameData, state == WHITE);
                }
                result = "";
            }
        } catch (IOException e) {
            return result;
        }
        return result;
    }

    private String leave() {
        state = LOGGED_IN;
        gameData = null;
        // TODO: Send leave to websocket
        return "Left game";
    }

    private String move(String[] param) {
        String result = "Couldn't make move";
        if (param.length == 3 || param.length == 4) {
            var startInput = param[1].toLowerCase();
            var endInput = param[2].toLowerCase();
            if (startInput.length() == 2 && endInput.length() == 2) {
                var startPos = new ChessPosition(startInput);
                var endPos = new ChessPosition(endInput);
                ChessPiece.PieceType promPiece;
                if (param.length == 4 && param[3].length() == 1) {
                    promPiece = switch (param[3].toLowerCase().charAt(0)) {
                        case 'q' -> ChessPiece.PieceType.QUEEN;
                        case 'r' -> ChessPiece.PieceType.ROOK;
                        case 'b' -> ChessPiece.PieceType.BISHOP;
                        case 'n' -> ChessPiece.PieceType.KNIGHT;
                        default -> null;
                    };
                } else {
                    promPiece = null;
                }
                var move = new ChessMove(startPos, endPos, promPiece);
                if (gameData.game().validMoves(startPos).contains(move)) {
                    try {
                        gameData.game().makeMove(move);
                        // TODO: send new board to websocket
                    } catch (Exception e) {
                        return result;
                    }
                    draw.drawBoard(gameData, (state == WHITE));
                    result = "";
                } else {
                    result = "Invalid move. Try another move, or use 'highlight'";
                }
            }
        }
        return result;
    }

    private String resign() {
        System.out.print("Are you sure you want to leave game? (y/N): ");
        var input = new Scanner(System.in).nextLine();
        if (input.equalsIgnoreCase("y")) {
            // TODO: send resign to websocket
            gameData = null;
            state = LOGGED_IN;
            return "Resigned game";
        } else {
            return "Cancelled resign";
        }
    }

    private String highlight(String[] param) {
        String result = "Couldn't check moves";
        try {
            if (param.length == 2) {
                var input = param[1].toLowerCase();
                if (input.length() == 2) {
                    var currPos = new ChessPosition(input);
                    var highlights = new ArrayList<ChessPosition>();
                    highlights.add(currPos);
                    for (var move : gameData.game().validMoves(currPos)) {
                        highlights.add(move.getEndPosition());
                    }
                    if (state == OBSERVER) {
                        draw.drawBoard(gameData, true, highlights, currPos);
                        draw.drawBoard(gameData, false, highlights, currPos);
                    } else {
                        draw.drawBoard(gameData, state == WHITE, highlights, currPos);
                    }
                    result = "";
                }
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public void loadGame(GameData newGameData) {
        gameData = newGameData;
        System.out.println("\n");
        draw.drawBoard(gameData, (state == WHITE));
        writePrompt();
    }

    public void notification(NotificationMessage nm) {
        System.out.println("\n" + SET_TEXT_COLOR_YELLOW + nm.message + RESET_TEXT_COLOR);
        writePrompt();
    }

    public void error(ErrorMessage e) {
        System.out.println("\n" + SET_TEXT_COLOR_RED + e.message + RESET_TEXT_COLOR);
        writePrompt();
    }
}
