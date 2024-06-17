import ui.ChessClient;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {

        try {
            var server  = args.length > 0 ? args[0] : "localhost";
            int port = args.length > 0 ? Integer.parseInt(args[1]) : 8080;

            ChessClient chessClient = new ChessClient(server, port);
            System.out.println("♕ CS240 Chess Client");
            System.out.println("Type 'Help' to get started");
            Scanner scanner = new Scanner(System.in);

            var result = "";
            while (!result.equals("quit")) {
                chessClient.writePrompt();
                String input = scanner.nextLine();
                result = chessClient.readInput(input);
                System.out.println(RESET_TEXT_COLOR + result);
            }
        } catch (Exception e) {
            System.out.println("Failed to connect to the server");
        }
    }
}