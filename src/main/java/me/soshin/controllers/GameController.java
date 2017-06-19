package me.soshin.controllers;

import me.soshin.dto.CreateGameResult;
import me.soshin.dto.JoinGameRequest;
import me.soshin.dto.MoveRequest;
import me.soshin.dto.NewGameRequest;
import me.soshin.models.Game;
import me.soshin.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Allows creating a new Kalah game
     * @param request
     * @return
     */
    @PostMapping("/newGame")
    public ResponseEntity<CreateGameResult> createGame(@RequestBody NewGameRequest request) {

        String gameId = gameService.createGame(request.getGameName());

        String playerId = gameService.assignPlayer(gameId, request.getPlayerName());

        return new ResponseEntity<>(new CreateGameResult(gameId, playerId), HttpStatus.CREATED);
    }

    /**
     * Allows second player to join the game
     * @param request
     * @return
     */
    @PostMapping("/joinGame")
    public String joinGame(@RequestBody JoinGameRequest request) {

        return gameService.assignPlayer(request.getGameId(), request.getPlayerName());
    }

    /**
     * Moves seeds from selected bin
     * Move may finish the game
     * @param request
     * @return current game state
     */
    @PostMapping("/move")
    public Game makeMove(@RequestBody MoveRequest request) {

        return gameService.makeMove(request.getGameId(), request.getPlayerId(), request.getBin());
    }

    /**
     * Return current game state
     * @param gameId
     * @return
     */
    @GetMapping("/game/{gameId}")
    public Game getGameState(@PathVariable("gameId") String gameId) {
        return gameService.getGame(gameId);
    }

}
