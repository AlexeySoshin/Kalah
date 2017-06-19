package me.soshin.exceptions;

public class PlayerOutOfTurnException extends RuntimeException {
    private final String gameId;
    private final String playerId;

    public PlayerOutOfTurnException(String gameId, String playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }
}
