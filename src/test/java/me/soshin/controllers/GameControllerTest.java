package me.soshin.controllers;

import me.soshin.dto.CreateGameResult;
import me.soshin.dto.JoinGameRequest;
import me.soshin.dto.MoveRequest;
import me.soshin.dto.NewGameRequest;
import me.soshin.models.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerTest {

    private CreateGameResult gameResponse;
    private ResponseEntity<CreateGameResult> responseEntity;

    @Autowired
    private TestRestTemplate template;
    private ResponseEntity<String> joinPlayerResponse;

    @Before
    public void setup() {
        NewGameRequest gameReq = new NewGameRequest("My Game", "Alice");

        responseEntity = template.postForEntity("/newGame", new HttpEntity<>(gameReq),
                        CreateGameResult.class, Collections.emptyMap());
        gameResponse = responseEntity.getBody();

        JoinGameRequest joinReq = new JoinGameRequest(gameResponse.getGameId(), "Bob");
        joinPlayerResponse = template.postForEntity("/joinGame", joinReq, String.class, Collections.emptyMap());

    }

    @Test
    public void createGame() throws Exception {
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(36, gameResponse.getPlayerId().length());
        assertEquals(36, gameResponse.getGameId().length());
    }

    @Test
    public void joinGame() throws Exception {
        assertEquals(HttpStatus.OK, joinPlayerResponse.getStatusCode());
    }

    @Test
    public void move() throws Exception {

        MoveRequest req = new MoveRequest(gameResponse.getGameId(), gameResponse.getPlayerId(), 0);
        ResponseEntity<String> response = template.postForEntity("/move", new HttpEntity<>(req), String.class, gameResponse.getGameId());

        assertEquals(HttpStatus.OK, response.getStatusCode());


        assertThat(response.getBody(), containsString("Alice"));
        assertThat(response.getBody(), containsString("6  6  6  6  6  6"));
        assertThat(response.getBody(), containsString("0<-P2      P1->1"));
        assertThat(response.getBody(), containsString("0  7  7  7  7  7"));
        assertThat(response.getBody(), containsString("Bob"));
    }
}