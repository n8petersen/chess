package ui;

import chess.ChessGame;
import model.GameData;

import static ui.EscapeSequences.*;
import static ui.State.*;

public class ChessClient {

    final ServerFacade server;
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

        if (state != LOGGED_IN) {
            return switch (params[0]) {
                case "help" -> help();
                case "quit" -> quit();
                case "register" -> register(params);
                case "login" -> login(params);
                case "draw" -> draw.drawBoard();
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
                case "draw" -> draw.drawBoard();
                default -> result;
            };
        }
    }

    private String register(String[] param) throws Exception {
        String result = "Couldn't register. Try again";
        if (param.length == 4 && state == LOGGED_OUT) {
            var resp = server.register(param[1], param[2], param[3]);
            username = param[1];
            authToken = resp.authToken();
            state = LOGGED_IN;
            result = "Created and logged in as " + param[1];
        }
        return result;
    }

    private String login(String[] param) throws Exception {
        String result = "";
        if (param.length == 3) {
            var resp = server.login(param[1], param[2]);
            username = param[1];
            authToken = resp.authToken();
            state = LOGGED_IN;
            result = "Logged in as " + param[1];
        }
        return result;
    }

    private String logout() throws Exception {
        server.logout(authToken);
        state = LOGGED_OUT;
        return "Logged out";
    }

    private String list() throws Exception {
        StringBuilder result = new StringBuilder();
        var games = server.listGames(authToken);
        gameList = games.games();
        for (int i = 0; i < gameList.length; i++) {
            var game = gameList[i];
            result.append(String.format("%d - %s - W:%s B:%s\n",i+1,game.gameName(),game.whiteUsername(), game.blackUsername()));
        }
        return result.toString();
    }

    private String join(String[] param) throws Exception {
        String result = "Couldn't join game, check command and try again";
        if (param.length == 3 && (param[2].equalsIgnoreCase("WHITE") || param[2].equalsIgnoreCase("BLACK"))) {
            int gameId = Integer.parseInt(param[1]);
            gameId = gameList[gameId - 1].gameID();
            var color = ChessGame.TeamColor.valueOf(param[2].toUpperCase());
            gameData = server.joinGame(authToken, gameId, color);
            state = (color == ChessGame.TeamColor.WHITE ? WHITE : BLACK);
            result = "Joined game " + gameData.gameID() + " as " + color;
            draw.drawBoard();
        }
        return result;
    }

    private String observe(String[] param) {
        String result = "";
        if (param.length == 2) {
            int gameId = Integer.parseInt(param[1]);
            gameId = gameList[gameId - 1].gameID();
            state = OBSERVER;
            result = "Joined game " + gameId + " as OBSERVER";
            draw.drawBoard();
        }
        return result;
    }

    private String create(String[] param) throws Exception {
        String result = "";
        if (param.length == 2) {
            gameData = server.createGame(authToken, param[1]);
            result = "Created game " + gameData.gameID();
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
                result.append(SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " - get possible commands");
                break;
            case OBSERVER:
                break;
            case BLACK, WHITE:
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

}
