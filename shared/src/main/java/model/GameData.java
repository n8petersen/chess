package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game, State state) {

    public enum State {
        WHITE,
        BLACK,
        DRAW,
        UNKNOWN
    }


    public GameData setWhite(String username) {
        return new GameData(this.gameID, username, this.blackUsername, this.gameName, this.game, this.state);
    }

    public GameData setBlack(String username) {
        return new GameData(this.gameID, this.whiteUsername, username, this.gameName, this.game, this.state);
    }

    public GameData setState(State newState) {
        return new GameData(this.gameID, this.whiteUsername, this.blackUsername, this.gameName, this.game, newState);
    }
}
