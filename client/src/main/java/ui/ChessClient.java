package ui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

import static ui.EscapeSequences.*;
import static ui.State.*;

public class ChessClient {

    private static final Logger log = LoggerFactory.getLogger(ChessClient.class);
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

        return switch (input_array[0]) {
            case "help" -> help();
            case "quit" -> quit();
            case "register" -> register(input_array);
            case "logout" -> logout();
            case "login" -> login(input_array);
            default -> result;
        };
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

    private String login(String[] param) throws Exception {
        String result = "";
        if (param.length == 3 && state == LOGGED_OUT) {
            var resp = server.login(param[1], param[2]);
            authToken = resp.authToken();
            state = LOGGED_IN;
            result = "Logged in as " + param[1];
        }
        return result;
    }

    private String logout() throws Exception {
        String result = "Couldn't logout.";
        if (state == LOGGED_IN) {
            server.logout(authToken);
            state = LOGGED_OUT;
            result = "Logged out.";
        }
        return result;
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

    private String quit () {
        return "quit";
    }
}
