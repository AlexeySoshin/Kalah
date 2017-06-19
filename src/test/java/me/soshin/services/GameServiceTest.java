package me.soshin.services;

import me.soshin.exceptions.PlayerOutOfTurnException;
import me.soshin.exceptions.bin.BinDoesntBelongToPlayerException;
import me.soshin.exceptions.game.GameAlreadyFullException;
import me.soshin.exceptions.game.GameNotStartedException;
import me.soshin.exceptions.game.NoSuchGameException;
import me.soshin.models.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

    @Autowired
    GameService service;

    private String gameId;

    @Before
    public void setUp() throws Exception {
        this.gameId = service.createGame("test game");
    }

    @Test
    public void createGame() throws Exception {

        Game game = service.getGame(gameId);
        // Should create new game with no players
        assertNull(game.getCurrentPlayer());
        assertNull(game.getFirstPlayer());
        assertNull(game.getSecondPlayer());
    }

    @Test
    public void assignFirstPlayer() throws Exception {

        // Should assign first player if no players yet

        service.assignPlayer(gameId, "Alice");

        Game game = service.getGame(gameId);

        assertEquals("Alice", game.getFirstPlayer().getName());
        assertEquals("Alice", game.getCurrentPlayer().getName());
        assertNull(game.getSecondPlayer());
    }

    @Test(expected = NoSuchGameException.class)
    public void assignPlayerWrongGame() {
        service.assignPlayer(UUID.randomUUID().toString(), "Alice");
    }

    @Test(expected = PlayerOutOfTurnException.class)
    public void outOfOrderMove() {
        service.assignPlayer(gameId, "Alice");
        String bobId = service.assignPlayer(gameId, "Bob");


        service.makeMove(gameId, bobId, 7);
    }

    @Test(expected = BinDoesntBelongToPlayerException.class)
    public void makeMove() {
        String aliceId = service.assignPlayer(gameId, "Alice");
        String bobId = service.assignPlayer(gameId, "Bob");


        service.makeMove(gameId, aliceId, 0);
        service.makeMove(gameId, aliceId, 1);
        service.makeMove(gameId, bobId, 0);
    }

    @Test
    public void makeValidMoves() {
        String aliceId = service.assignPlayer(gameId, "Alice");
        String bobId = service.assignPlayer(gameId, "Bob");


        service.makeMove(gameId, aliceId, 0);
        service.makeMove(gameId, aliceId, 1);
        Game gameState = service.makeMove(gameId, bobId, 7);


        System.out.println(gameState.toString());
    }

    @Test(expected = GameNotStartedException.class)
    public void moveBeforeGameStart() {
        String aliceId = service.assignPlayer(gameId, "Alice");


        service.makeMove(gameId, aliceId, 1);
    }

    @Test
    public void assignSecondPlayer() throws Exception {
        // Should assign second player if first player present
        service.assignPlayer(gameId, "Alice");
        service.assignPlayer(gameId, "Bob");

        Game game = service.getGame(gameId);

        assertEquals("Alice", game.getFirstPlayer().getName());
        assertEquals("Alice", game.getCurrentPlayer().getName());
        assertEquals("Bob", game.getSecondPlayer().getName());
    }

    @Test(expected = GameAlreadyFullException.class)
    public void assignThirdPlayer() throws Exception {
        // Should not assign player if both players present
        service.assignPlayer(gameId, "Alice");
        service.assignPlayer(gameId, "Bob");
        service.assignPlayer(gameId, "Eve");
    }

}