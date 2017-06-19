package me.soshin.dto;

/**
 * Parameters for playing the next move
 */
public class MoveRequest {
    private String gameId;
    private String playerId;
    private Integer bin;

    public MoveRequest() {

    }

    public MoveRequest(String gameId, String playerId, int bin) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.bin = bin;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Integer getBin() {
        return bin;
    }

    public void setBin(Integer bin) {
        this.bin = bin;
    }
}
