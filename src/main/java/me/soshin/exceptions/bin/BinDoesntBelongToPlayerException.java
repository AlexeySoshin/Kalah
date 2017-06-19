package me.soshin.exceptions.bin;

import me.soshin.models.Player;

public class BinDoesntBelongToPlayerException extends RuntimeException {
    public BinDoesntBelongToPlayerException(Player currentPlayer) {
    }
}
