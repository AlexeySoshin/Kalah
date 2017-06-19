package me.soshin.models;

import me.soshin.exceptions.bin.BinEmptyException;
import me.soshin.exceptions.bin.BinIsKalahException;
import me.soshin.exceptions.bin.BinOutOfRangeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class BoardTest {

    private String sep =  System.lineSeparator();
    private Board b;

    @Test
    public void printBoard() throws Exception {

        Board b = new Board();

        assertEquals(" 6  6  6  6  6  6 " + sep +
                "                  " + sep +
                " 0<-P2      P1->0" + sep +
                "               " + sep +
                " 6  6  6  6  6  6 " + sep +
                " ^                ", b.toString());
    }


    @Test
    public void moveFirstPlayer() throws Exception {

        Integer lastBin = b.move(0);

        assertEquals(Board.FIRST_PLAYER_KALAH, (int) lastBin);

        assertEquals(" 6  6  6  6  6  6 " + sep +
                "                  " + sep +
                " 0<-P2      P1->1" + sep +
                "                ^" + sep +
                " 0  7  7  7  7  7 " + sep +
                "                  ", b.toString());
    }

    @Before
    public void setup() {
        this.b = new Board();
    }

    @Test(expected = BinEmptyException.class)
    public void moveEmptyBin() {
        b.move(0);
        b.move(0);
    }

    @Test(expected = BinIsKalahException.class)
    public void moveFirstPlayerKalah() {
        b.move(6);
    }

    @Test(expected = BinIsKalahException.class)
    public void moveSecondPlayerKalah() {
        b.move(13);
    }

    @Test(expected = BinOutOfRangeException.class)
    public void moveNegativeBin() {
        b.move(-1);
    }

    @Test(expected = BinOutOfRangeException.class)
    public void moveOutOfRangeBin() {
        b.move(14);
    }

    @Test
    public void moveSecondPlayer() {
        Board b = new Board();

        b.move(12);

        System.out.println(b.toString());

        assertEquals(" 0  6  6  6  6  6 " + sep +
                "                  " + sep +
                " 1<-P2      P1->0" + sep +
                "               " + sep +
                " 7  7  7  7  7  6 " + sep +
                "             ^    ", b.toString());
    }

    @Test
    public void validateLastBin() {
        Board b = new Board();

        Integer lastBin = b.move(0);

        // If we're starting from the leftmost bin, last bin should be the Kalah of the first player
        assertEquals(Board.FIRST_PLAYER_KALAH, (int)lastBin);
    }

    @Test
    public void tryCaptureNotCapturedBin() {
        Board b = new Board();

        System.out.println(b.toString());

        assertFalse(b.tryCapture(0));
    }

    @Test
    public void tryCaptureCapturedBin() {
        TestBoard b = new TestBoard();


        b.playMoves(0, 1, 7, 0);


        assertTrue(b.tryCapture(1));

        System.out.println(b.toString());

        assertEquals(" 7  0  7  7  8  0 " + sep +
                "                  " + sep +
                " 1<-P2      P1->10" + sep +
                "               " + sep +
                " 0  0  8  8  8  8 " +sep +
                "    ^             ", b.toString());
    }

    @Test
    public void tryCaptureCapturedBinSecondPlayer() {
        TestBoard b = new TestBoard();


        b.playMoves(1, 8, 5, 9, 1, 8);


        assertTrue(b.tryCapture(9));

        System.out.println(b.toString());

        assertEquals(" 9  9  9  0  0  8 " + sep +
                "          ^       " +sep +
                " 11<-P2      P1->2" +sep +
                "               " +sep +
                " 8  0  9  0  7  0 " + sep +
                "                  ", b.toString());
    }

    @Test
    public void addToYourKalahOnly() {
        TestBoard b = new TestBoard();


        b.playMoves(0, 1, 7, 5);


        System.out.println(b.toString());

        assertEquals(" 8  8  8  8  9  1 " + sep +
                "                  " +sep +
                " 1<-P2      P1->3" +sep +
                "               " +sep +
                " 2  0  8  8  8  0 " +sep +
                " ^                ", b.toString());
    }


    private class TestBoard extends Board {
        public void playMoves(Integer... moves) {
            System.out.println(this.toString());
            System.out.println();
            for (int i = 0; i < moves.length; i++) {
                this.move(moves[i]);
                System.out.println(this.toString());
                System.out.println();
            }
        }
    }
}