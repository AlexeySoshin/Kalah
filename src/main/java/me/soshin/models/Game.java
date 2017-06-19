package me.soshin.models;

import me.soshin.exceptions.bin.BinDoesntBelongToPlayerException;
import me.soshin.exceptions.game.GameNotStartedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Game {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final String id;
    private final String name;

    private Player firstPlayer;
    private Player secondPlayer;
    private Board board;
    private Player currentPlayer;
    private String result = "Not completed";

    public Game() {
        this("");
    }

    public Game(String gameName) {
        this.name = gameName;
        this.id =  UUID.randomUUID().toString();
        this.board = new Board();
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public String setFirstPlayer(String firstPlayerName) {

        this.firstPlayer = new Player(firstPlayerName);

        this.currentPlayer = this.firstPlayer;

        return this.currentPlayer.getId();
    }

    public String setSecondPlayer(String playerName) {
        this.secondPlayer = new Player(playerName);
        return this.secondPlayer.getId();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     *
     */
    private void switchTurn() {
        if (!hasStarted()) {
            throw new GameNotStartedException(this.id);
        }
        else {
            if (isFirstPlayerTurn()) {
                currentPlayer = secondPlayer;
            }
            else {
                currentPlayer = firstPlayer;
            }
        }
    }

    private boolean isFirstPlayerTurn() {
        return currentPlayer == null || currentPlayer.equals(firstPlayer);
    }

    public boolean hasStarted() {
        return this.firstPlayer != null && this.secondPlayer != null;
    }

    /**
     *
     * @param bin
     * @return true if move completed the game
     */
    public boolean makeMove(Integer bin) {

        if (!doesCellBelongToPlayer(bin)) {
            throw new BinDoesntBelongToPlayerException(currentPlayer);
        }

        Integer lastCell = board.move(bin);

        if (doesCellBelongToPlayer(lastCell) && board.tryCapture(lastCell)) {
            switchTurn();
        }
        else if (isCurrentPlayerKalah(lastCell)) {
            log.info("Free turn for player " + currentPlayer);
        }
        else {
            switchTurn();
        }

        if (gameEnded()) {
            board.endGame();
            computeGameResult();
            return true;
        }

        return false;
    }

    private void computeGameResult() {
        int firstPlayerScore = board.getFirstPlayerScore();
        int secondPlayerScore = board.getSecondPlayerScore();
        if (firstPlayerScore > secondPlayerScore) {
            this.result = "First player won";
        }
        else if (firstPlayerScore < secondPlayerScore) {
            this.result = "Second player won";
        }
        else {
            this.result = "Draw";
        }
    }

    private boolean gameEnded() {
        return board.getFirstPlayerSeeds() == 0 || board.getSecondPlayerSeeds() == 0;
    }

    private boolean isCurrentPlayerKalah(Integer lastCell) {
        return doesCellBelongToPlayer(lastCell) && board.isKalah(lastCell);
    }

    /**
     *
     * @param cell
     * @return
     */
    private boolean doesCellBelongToPlayer(Integer cell) {

        if (isFirstPlayerTurn()) {
            if (cell > Board.BINS_PER_PLAYER) {
                return false;
            }
        }
        else {
            if (cell < Board.BINS_PER_PLAYER + 1) {
                return false;
            }
        }

        return true;
    }

    public String getBoard() {
        return this.board.toString();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String getResult() {
        return result;
    }
}
