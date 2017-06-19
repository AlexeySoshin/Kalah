package me.soshin.models;

import me.soshin.exceptions.bin.BinDoesntBelongToPlayerException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class GameTest {

    private Game g;

    @Before
    public void setup() {
        this.g = new Game("Test game");

        String aliceId = g.setFirstPlayer("Alice");
        String bobId = g.setSecondPlayer("Bob");
    }

    @Test(expected = BinDoesntBelongToPlayerException.class)
    public void makeMoveNotOnYourBin() throws Exception {

        g.makeMove(7);
    }

    @Test
    public void makeFreeMove() {

        assertEquals("Alice", g.getCurrentPlayer().getName());

        g.makeMove(0);

        assertEquals("Alice", g.getCurrentPlayer().getName());
    }


    @Test
    public void moveSwitchesPlayers() {
        assertEquals("Alice", g.getCurrentPlayer().getName());

        g.makeMove(1);

        assertEquals("Bob", g.getCurrentPlayer().getName());
    }

    @Test
    public void captureSwitchesPlayers() {
        assertEquals("Alice", g.getCurrentPlayer().getName());

        g.makeMove(2);
        g.makeMove(12);
        g.makeMove(3);
        g.makeMove(7);
        g.makeMove(2);

        assertEquals("Bob", g.getCurrentPlayer().getName());
    }
}