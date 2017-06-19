package me.soshin.dto;

/**
 * Parameters for creating a new game
 */
public class NewGameRequest {
    private String playerName;
    private String gameName;

    public NewGameRequest() {

    }

    public NewGameRequest(String gameName, String playerName) {
        this.gameName = gameName;
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}