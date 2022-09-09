package com.hometask.montyhall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hometask.montyhall.dto.ChooseDecision;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameResult;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.service.GameResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameResultControllerIT {

    private final GameResultService gameResultService = mock(GameResultService.class);

    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        GameResultController gameResultController = new GameResultController(gameResultService);

        mvc = MockMvcBuilders
                .standaloneSetup(gameResultController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .build();
    }

    @Test
    void should_create_game_result_when_change_picked_box_is_true() throws Exception {
        boolean changePickedBox = true;
        ChooseDecision chooseDecision = new ChooseDecision();
        chooseDecision.setChangePickedBox(changePickedBox);

        long gameId = 1L;
        Game game = new Game();
        game.setId(gameId);
        game.setStatus(GameStatus.IN_PROGRESS);

        long gameResultId = 4L;
        GameResult gameResult = new GameResult();
        gameResult.setId(gameResultId);
        gameResult.setGame(game);
        gameResult.setPickedBoxWasChanged(changePickedBox);
        gameResult.setWin(true);

        when(gameResultService.createGameResult(gameId, changePickedBox)).thenReturn(gameResult);

        String response = mvc.perform(post("/games/" + gameId + "/game-results")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chooseDecision)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GameResult actualGameResult = objectMapper.readValue(response, GameResult.class);
        assertThat(actualGameResult).isEqualTo(gameResult);

        verify(gameResultService).createGameResult(gameId, changePickedBox);
    }

}
