package me.soshin.exceptions.game;

public class GameNotStartedException extends RuntimeException {
    private final String gameId;

    public GameNotStartedException(String gameId) {
        this.gameId = gameId;
    }
}
