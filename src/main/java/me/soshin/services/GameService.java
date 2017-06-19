package me.soshin.services;

import me.soshin.exceptions.PlayerOutOfTurnException;
import me.soshin.exceptions.game.GameAlreadyFullException;
import me.soshin.exceptions.game.GameNotStartedException;
import me.soshin.exceptions.game.NoSuchGameException;
import me.soshin.models.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Map<String, Game> games = new ConcurrentHashMap<>();

    public String createGame(String gameName) {

        Game game = new Game(gameName);

        games.put(game.getId(), game);

        return game.getId();
    }

    /**
     * Assigns player to a game
     * This may be the first or second player
     * @param gameId
     * @param playerName
     * @return
     */
    public String assignPlayer(String gameId, String playerName) {
        Game game = getGame(gameId);

        if (game.getFirstPlayer() == null) {
            return game.setFirstPlayer(playerName);
        }
        else if (game.getSecondPlayer() == null) {
            return game.setSecondPlayer(playerName);
        }
        else {
            throw new GameAlreadyFullException(gameId);
        }
    }

    /**
     * Makes the next move, and remove the game, if it was finished
     * @param gameId
     * @param playerId
     * @param cell
     * @return current state of the game
     */
    public Game makeMove(String gameId, String playerId, Integer cell) {
        Game game = getGame(gameId);

        if (!game.hasStarted()) {
            throw new GameNotStartedException(gameId);
        }

        if (!game.getCurrentPlayer().getId().equals(playerId)) {
            throw new PlayerOutOfTurnException(gameId, playerId);
        }

        boolean isFinal = game.makeMove(cell);
        if (isFinal) {
            log.info(String.format("Game '%s' completed!", game.getName()));
            games.remove(gameId);
        }

        return game;
    }

    public Game getGame(String gameId) {
        Game g = games.get(gameId);

        if (g == null) {
            throw new NoSuchGameException(gameId);
        }

        return g;
    }
}
