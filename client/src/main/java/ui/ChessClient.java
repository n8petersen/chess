package ui;

import chess.ChessGame;

import static ui.EscapeSequences.*;
import static ui.State.*;

public class ChessClient {

    final ServerFacade server;
    private State state = State.LOGGED_OUT;
    private String authToken;

    public ChessClient(String host, int port) {
        server = new ServerFacade(host, port);
    }

    public String readInput(String input) throws Exception {
        var result = "Couldn't process command: " + input + ". Try running 'Help'";
        input = input.toLowerCase();
        var input_array = input.split(" ");

        if (state != LOGGED_IN) {
            return switch (input_array[0]) {
                case "help" -> help();
                case "quit" -> quit();
                case "register" -> register(input_array);
                case "login" -> login(input_array);
                default -> result;
            };
        } else {
            return switch (input_array[0]) {
                case "help" -> help();
                case "quit" -> quit();
                case "logout" -> logout();
                case "list" -> list();
                case "join" -> join(input_array);
                case "observe" -> observe(input_array);
                case "create" -> create(input_array);
                default -> result;
            };
        }
    }

    private String register(String[] param) throws Exception {
        String result = "Couldn't register. Try again";
        if (param.length == 4 && state == LOGGED_OUT) {
            var resp = server.register(param[1], param[2], param[3]);
            authToken = resp.authToken();
            state = LOGGED_IN;
            result = "Created and logged in as " + param[1];
        }
        return result;
    }

    private String list() throws Exception {
        String result = "";
        var resp = server.listGames(authToken);
        result = resp.toString();
        // TODO: FORMAT THIS ACCORDINGLY:
        //  Lists all the games that currently exist on the server
        //  Calls the server list API to get all the game data, and displays the games in a numbered list,
        //  including the game name and players (not observers) in the game.
        //  The numbering for the list should be independent of the game IDs.
        return result;
    }

    private String join(String[] param) throws Exception {
        String result = "Couldn't join game, check command and try again";
        if (param.length == 3 && (param[2].equalsIgnoreCase("WHITE") || param[2].equalsIgnoreCase("BLACK"))) {
            int gameId = Integer.parseInt(param[1]);
            var color = ChessGame.TeamColor.valueOf(param[2].toUpperCase());
            var resp = server.joinGame(authToken, gameId, color);
            state = (color == ChessGame.TeamColor.WHITE ? WHITE : BLACK);
            result = "Joined game " + resp.gameID() + " as " + color;
        }
        return result;
    }

    private String observe(String[] param) {
        String result = "";
        if (param.length == 2) {
            int gameId = Integer.parseInt(param[1]);
            state = OBSERVER;
            result = "Joined game " + gameId + " as OBSERVER";
        }
        return result;
    }

    private String create(String[] param) throws Exception {
        String result = "";
        if (param.length == 2) {
            var resp = server.createGame(authToken, param[1]);
            result = "Created game " + resp.gameID();
        }
        return result;
    }

    private String login(String[] param) throws Exception {
        String result = "";
        if (param.length == 3) {
            var resp = server.login(param[1], param[2]);
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

    private String help() {
        StringBuilder result = new StringBuilder();
        switch (state) {
            case LOGGED_IN:
                result.append(SET_TEXT_COLOR_BLUE + "list" + RESET_TEXT_COLOR + " - get existing games" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]" + RESET_TEXT_COLOR + " - join a game (as color)" + "\n");
                result.append(SET_TEXT_COLOR_BLUE + "observe <ID>" + RESET_TEXT_COLOR + " - observe specific game" + "\n");
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

    private String quit () throws Exception {
        logout();
        return "quit";
    }
}
