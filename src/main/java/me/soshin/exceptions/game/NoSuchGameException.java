package me.soshin.exceptions.game;


public class NoSuchGameException extends RuntimeException {
    private final String gameId;

    public NoSuchGameException(String gameId) {
        this.gameId = gameId;
    }
}
