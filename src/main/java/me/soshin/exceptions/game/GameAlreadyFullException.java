package me.soshin.exceptions.game;


public class GameAlreadyFullException extends RuntimeException {
    private final String gameId;

    public GameAlreadyFullException(String gameId) {
        this.gameId = gameId;
    }
}
