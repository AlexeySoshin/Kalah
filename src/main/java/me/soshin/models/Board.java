package me.soshin.models;


import me.soshin.exceptions.bin.BinEmptyException;
import me.soshin.exceptions.bin.BinIsKalahException;
import me.soshin.exceptions.bin.BinOutOfRangeException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Board {


    public static final int BINS_PER_PLAYER = 6;
    public static final int FIRST_PLAYER_KALAH = BINS_PER_PLAYER;
    private static final int SECOND_PLAYER_KALAH = FIRST_PLAYER_KALAH * 2 + 1;

    private static final int SIZE = BINS_PER_PLAYER*2+2;
    private static final int SEEDS_PER_PLAYER = 6;
    private int lastBin;

    @Override
    public String toString() {
        return (new BoardPrinter()).print();
    }

    /*
         Bins looks as follows
         0  1  2  3  4  5  6        7  8  9  10 11 12 13
         P1 P1 P1 P1 P1 P1 P1_KALAH P2 P2 P2 P2 P2 P2 P2_KALAH
         */
    private List<AtomicInteger> bins = new ArrayList<>(SIZE);


    public Board(Integer... values) {
        for (int i = 0; i < Math.min(SIZE, values.length); i++) {
            bins.add(new AtomicInteger(values[i]));
        }
    }

    public Board() {
        for (int i = 0; i < SIZE; i++) {
            if (i == FIRST_PLAYER_KALAH || i == SECOND_PLAYER_KALAH) {
                bins.add(new AtomicInteger(0));
            }
            else {
                bins.add(new AtomicInteger(SEEDS_PER_PLAYER));
            }
        }
    }

    /**
     * Move seeds, starting from given bin
     * @param bin starting bin
     * @return last bin the seed has landed into
     */
    public Integer move(Integer bin) {

        validate(bin);

        int seeds = bins.get(bin).get();

        // Empty current bin
        bins.get(bin).set(0);
        boolean secondKalah = false;

        // Spread seeds from current bins to all the next bins
        int i = 1;
        while (seeds > 0) {
            int currentBin = (i + bin) % (SIZE);
            this.lastBin= currentBin;

            // Don't add seeds to other players kalah
            if (!isKalah(currentBin) || !secondKalah) {
                seeds--;
                bins.get(currentBin).incrementAndGet();
            }

            if (isKalah(currentBin)) {
                secondKalah = !secondKalah;
            }
            i++;
        }

        return this.lastBin;
    }

    private void validate(Integer bin) {
        if (bin < 0 || bin >= SIZE) {
            throw new BinOutOfRangeException(bin);
        }
        else if (isKalah(bin)) {
            throw new BinIsKalahException(bin);
        }
        else if (isEmpty(bin)) {
            throw new BinEmptyException(bin);
        }
    }

    public boolean isKalah(Integer bin) {
        return bin == FIRST_PLAYER_KALAH || bin == SECOND_PLAYER_KALAH;
    }

    public boolean isEmpty(Integer bin) {
        return bins.get(bin).get() == 0;
    }

    private Integer getOpposite(Integer bin) {

        if (isKalah(bin)) {
            throw new BinIsKalahException(bin);
        }

        return BINS_PER_PLAYER * 2 - bin;
    }

    /**
     * If there's exactly one seed in the last landed bin, it's a tryCapture
     * @param lastBin
     * @return
     */
    public boolean isCapture(Integer lastBin) {
        return bins.get(lastBin).get() == 1 && !isKalah(lastBin);
    }

    /**
     *
     * @param bin last bin
     * @return true for successful tryCapture
     */
    public boolean tryCapture(Integer bin) {

        if (!isCapture(bin)) {
            return false;
        }

        Integer oppositeBin = getOpposite(bin);

        int seeds = 1 + bins.get(oppositeBin).get();
        int kalah = FIRST_PLAYER_KALAH;
        if (bin > BINS_PER_PLAYER) {
            kalah = SECOND_PLAYER_KALAH;
        }
        bins.get(kalah).addAndGet(seeds);

        bins.get(bin).set(0);
        bins.get(oppositeBin).set(0);

        return true;
    }

    public int getFirstPlayerSeeds() {
        return countSeeds(0, BINS_PER_PLAYER);
    }

    public int getSecondPlayerSeeds() {
        return countSeeds(BINS_PER_PLAYER + 1, SIZE - 1);
    }

    private int countSeeds(int from, int to) {
        int count = 0;

        for (int i = from; i < to; i++) {
            count += bins.get(i).get();
        }

        return count;
    }

    /**
     * Empty bins of both players
     */
    public void endGame() {
        emptyBins(0, BINS_PER_PLAYER, FIRST_PLAYER_KALAH);
        emptyBins(BINS_PER_PLAYER + 1, SIZE - 1, SECOND_PLAYER_KALAH);
    }

    /**
     * Empty range of bins into kalah at the end of the game
     * @param from
     * @param to
     * @param into
     */
    private void emptyBins(int from, int to, int into) {
        for (int i = from; i < to; i++) {
            bins.get(into).addAndGet(bins.get(i).get());
            bins.get(i).set(0);
        }
    }

    public int getFirstPlayerScore() {
        return bins.get(FIRST_PLAYER_KALAH).get();
    }

    public int getSecondPlayerScore() {
        return bins.get(SECOND_PLAYER_KALAH).get();
    }

    /**
     * Prints the board in ASCII
     */
    private class BoardPrinter {
        public String print() {

            StringBuilder sb = new StringBuilder();

            printSecondPlayerBins(bins, sb);

            printSecondPlayerPointer(lastBin, sb);

            printPlayerKalahs(bins, sb);

            printKalahPointer(lastBin, sb);

            printFirstPlayerBins(bins, sb);

            printFirstPlayerPointer(sb);

            return sb.toString();
        }

        private void printFirstPlayerPointer(StringBuilder sb) {
            for (int i = 0; i < BINS_PER_PLAYER; i++) {
                if (i == lastBin) {
                    sb.append(" ^ ");
                }
                else {
                    sb.append("   ");
                }
            }
        }

        private void printSecondPlayerPointer(Integer lastBin, StringBuilder sb) {
            for (int i = SIZE - 2; i >= BINS_PER_PLAYER + 1; i--) {

                if (i == lastBin) {
                    sb.append(" ^ ");
                }
                else {
                    sb.append("   ");
                }
            }

            sb.append(System.lineSeparator());
        }

        private void printPlayerKalahs(List<AtomicInteger> bins, StringBuilder sb) {
            sb.append(" ").append(bins.get(SECOND_PLAYER_KALAH));

            sb.append("<-P2      P1->");

            sb.append(bins.get(FIRST_PLAYER_KALAH));

            sb.append(System.lineSeparator());
        }

        private void printFirstPlayerBins(List<AtomicInteger> bins, StringBuilder sb) {
            for (int i = 0; i < BINS_PER_PLAYER; i++) {
                if (bins.get(i).get() < 10) {
                    sb.append(" ");
                }
                sb.append(bins.get(i)).append(" ");
            }

            sb.append(System.lineSeparator());
        }

        private void printKalahPointer(Integer lastBin, StringBuilder sb) {
            if (FIRST_PLAYER_KALAH == lastBin) {
                sb.append("                ^");
            }
            else if (SECOND_PLAYER_KALAH == lastBin) {
                sb.append("^           ");
            }
            else {
                sb.append("               ");
            }

            sb.append(System.lineSeparator());
        }

        private void printSecondPlayerBins(List<AtomicInteger> bins, StringBuilder sb) {

            for (int i = SIZE - 2; i >= BINS_PER_PLAYER + 1; i--) {
                if (bins.get(i).get() < 10) {
                    sb.append(" ");
                }
                sb.append(bins.get(i)).append(" ");
            }

            sb.append(System.lineSeparator());
        }
    }
}
