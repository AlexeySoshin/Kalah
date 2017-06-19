# Kalah
Simple implementation of [Kalah](https://en.wikipedia.org/wiki/Kalah) game using SpringBoot

## Installation
```bash
./mvnw spring-boot:run
```

## APIs

### POST /newGame
Allows player to create a new game

```json
{
  "gameName": "My game",
  "playerName": "Alice"
}
```

### POST /joinGame
Allows another player to join the game

```json
{
  "gameId":"...",
  "playerName":"Bob"
}
```

### POST /move
Moves seeds from selected bin <br />
Move may finish the game

```json
{
    "gameId": "...", 
    "playerId":"...", 
    "bin": "between 0 and 13"
}
```

### GET /game/{gameId}
Returns current game state

```json
{
  "firstPlayer": "",
  "secondPlayer": "",
  "board": "ASCII representation of the board"
}
```

## License 
MIT