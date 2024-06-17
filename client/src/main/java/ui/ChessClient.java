package ui;

import chess.ChessGame;
import chess.ChessPosition;
import clientutil.ServerFacade;
import clientutil.State;
import model.GameData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static clientutil.State.*;

public class ChessClient {

    private final ServerFacade server;
    private State state = State.LOGGED_OUT;
    private String username;
    private String authToken;
    private GameData gameData;
    private GameData[] gameList;
    private final Draw draw = new Draw();

    public ChessClient(String host, int port) {
        server = new ServerFacade(host, port);
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
                    state = (color == ChessGame.TeamColor.WHITE ? WHITE : BLACK);
                    result = "Joined game " + gameData.gameID() + " as " + color;
                    var whiteOrientation = state == WHITE;
                    draw.drawBoard(gameData, whiteOrientation);
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
                var whiteOrientation = param[1].equalsIgnoreCase("white");
                if (gameList.length > 0) {
                    gameData = gameList[0];
                    draw.drawBoard(gameData, whiteOrientation);
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
                    var whiteOrientation = state == WHITE;
                    draw.drawBoard(gameData, whiteOrientation);
                }
                result = "";
            }
        } catch (IOException e) {
            return result;
        }
        return result;
    }

    private String leave() {
        String result = "Couldn't leave game";
        state = LOGGED_IN;
        result = "Left game";
        // remove player from the game
        return result;
    }

    private String move(String[] param) {
        String result = "Couldn't make move";
        if (param.length == 3) {
            var startPosition = param[1];
            var endPosition = param[2];
            // check that move is valid
            // if it is, then update the local game object and then send updated game
            // if it is not, alert the user that it is invalid and to make another move
            //   suggest 'highlight' command
        }
        return result;
    }

    private String resign() {
        String result = "Couldn't resign from game";
        System.out.print("Are you sure you want to leave game? (y/N): ");
        var input = new Scanner(System.in).nextLine();
        if (input.equalsIgnoreCase("y")) {
            result = "Resigned game";
            // set opponent to winner, end game
        } else {
            result = "Cancelled resign";
        }
        return result;
    }

    private String highlight(String[] param) {
        String result = "Couldn't check moves";
        if (param.length == 2) {
            var input = param[1].toLowerCase();
            if (input.length() == 2) {
                int col = input.charAt(0) - 'a' + 1;
                int row = input.charAt(1) - '1' + 1;
                var currPos = new ChessPosition(row, col);
                var highlights = new ArrayList<ChessPosition>();
                highlights.add(currPos);
                for (var move : gameData.game().validMoves(currPos)) {
                    highlights.add(move.endPosition());
                }
                if (state == OBSERVER) {
                    draw.drawBoard(gameData, true, highlights, currPos);
                    draw.drawBoard(gameData, false, highlights, currPos);
                } else {
                    var whiteOrientation = state == WHITE;
                    draw.drawBoard(gameData, whiteOrientation, highlights, currPos);
                }
                result = "";
            }
        }
        return result;
    }

}
