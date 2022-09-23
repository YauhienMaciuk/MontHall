package com.hometask.montyhall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hometask.montyhall.dto.GameDto;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GameControllerIT {

    private final GameService gameService = mock(GameService.class);

    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        GameController gameController = new GameController(gameService);

        mvc = MockMvcBuilders
                .standaloneSetup(gameController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .build();
    }

    @Test
    void should_create_game() throws Exception {
        GameDto gameDto = new GameDto();
        gameDto.setNumberOfBoxes(3);

        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.CREATED);

        when(gameService.createGame(gameDto)).thenReturn(game);

        String response = mvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Game actualGame = objectMapper.readValue(response, Game.class);
        assertThat(actualGame).isEqualTo(game);

        verify(gameService).createGame(gameDto);
    }

    @Test
    void should_not_create_game_without_number_of_boxes() throws Exception {
        GameDto gameDto = new GameDto();
        String expected = "[\"numberOfBoxes must be bigger than or equal 3\"]";

        String actual = mvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(expected);

        verifyNoInteractions(gameService);
    }

    @Test
    void should_not_create_game_when_number_of_boxes_bigger_then_allowed() throws Exception {
        GameDto gameDto = new GameDto();
        int numberOfBoxes = 1001;
        gameDto.setNumberOfBoxes(numberOfBoxes);
        String expected = "[\"numberOfBoxes must be less than or equal 1000\"]";

        String actual = mvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(expected);

        verifyNoInteractions(gameService);
    }

    @Test
    void should_find_game_by_id() throws Exception {
        Game game = new Game();
        long gameId = 1L;
        game.setId(gameId);
        game.setStatus(GameStatus.CREATED);
        when(gameService.findById(gameId)).thenReturn(game);

        String response = mvc.perform(MockMvcRequestBuilders
                .get("/games/" + gameId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Game actual = objectMapper.readValue(response, Game.class);
        assertThat(actual).isEqualTo(game);

        verify(gameService).findById(gameId);
    }

    @Test
    void should_not_find_game_when_game_does_not_exist() throws Exception {
        Long gameId = 1L;
        String errMsg = "Could not find the Game by id = " + gameId;
        String expected = "\"" + errMsg + "\"";
        when(gameService.findById(gameId)).thenThrow(new NoSuchEntityException(errMsg));

        String actual = mvc.perform(MockMvcRequestBuilders
                .get("/games/" + gameId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(expected);

        verify(gameService).findById(gameId);
    }

}
