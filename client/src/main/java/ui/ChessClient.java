package ui;

import chess.ChessGame;
import clientutil.ServerFacade;
import clientutil.State;
import model.GameData;

import java.io.IOException;

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
        } else {
            return switch (params[0]) {
                case "help" -> help();
                case "quit" -> quit();
                case "logout" -> logout();
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "create" -> create(params);
                case "draw" -> draw();
                default -> result;
            };
        }
    }

    private String register(String[] param) throws Exception {
        String result = "Couldn't register. Try again";
        try {
            if (param.length == 4 && state == LOGGED_OUT) {
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
                    draw.drawBoard();
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
                    gameId = gameList[gameId - 1].gameID();
                    state = OBSERVER;
                    result = "Joined game " + gameId + " as OBSERVER";
                    draw.drawBoard();
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
            if (param.length == 2 && state == LOGGED_IN) {
                gameData = server.createGame(authToken, param[1]);
                result = "Created game " + gameData.gameID();
            }
        } catch (IOException e) {
            return result;
        }
        return result;
    }

    private String help() {
        StringBuilder result = new StringBuilder();
        switch (state) {
            case LOGGED_IN, BLACK, WHITE, OBSERVER:
                result.append(SET_TEXT_COLOR_BLUE + "list" + RESET_TEXT_COLOR + " - get existing games" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]" + RESET_TEXT_COLOR + " - join a game (as color)" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "observe <ID>" + RESET_TEXT_COLOR + " - observe a game" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "create <NAME>" + RESET_TEXT_COLOR + " - create new game" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "logout" + RESET_TEXT_COLOR + " - logout of account" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " - close program" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " - get possible commands");
                break;
            default:
                result.append(SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR + " - create new account" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR + " - login to existing account" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " - close program" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " - get possible commands");
                break;
        }
        return result.toString();
    }

    private String quit() throws Exception {
        if (state == LOGGED_IN) {
            logout();
        }
        return "quit";
    }

    private String draw() throws Exception {
        String result = "Couldn't draw game";
        try {
            list();
            if (gameList.length > 0) {
                gameData = gameList[0];
                draw.drawBoard();
                result = "";
            }
        } catch (IOException e) {
            return result;
        }
        return result;
    }

}
