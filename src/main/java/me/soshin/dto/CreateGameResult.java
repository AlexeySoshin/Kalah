package me.soshin.dto;

/**
 * Output of creating a new game
 */
public class CreateGameResult {
    private String playerId;
    private String gameId;

    public CreateGameResult() {}

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public CreateGameResult(String gameId, String playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getGameId() {
        return gameId;
    }
}