package com.hometask.montyhall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hometask.montyhall.dto.GameDto;
import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.repository.BoxRepository;
import com.hometask.montyhall.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private BoxRepository boxRepository;

    @Test
    void createGameTest() throws Exception {
        GameDto gameDto = new GameDto();
        gameDto.setNumberOfBoxes(3);

        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.CREATED);

        Box box = new Box();
        box.setId(2L);
        box.setGame(game);
        box.setOpened(false);
        box.setPicked(false);
        box.setWinning(false);

        Mockito.when(gameRepository.save(any())).thenReturn(game);
        Mockito.when(boxRepository.save(any())).thenReturn(box);

        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(GameStatus.CREATED.toString()));
    }

    @Test
    void tryToCreateGameWithoutNumberOfBoxesTest() throws Exception {
        GameDto gameDto = new GameDto();

        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("[\"numberOfBoxes must be bigger than or equal 3\"]",
                        result.getResponse().getContentAsString()));
    }

    @Test
    void tryToCreateGameWhenNumberOfBoxesBiggerThen1000Test() throws Exception {
        GameDto gameDto = new GameDto();
        gameDto.setNumberOfBoxes(1001);

        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("[\"numberOfBoxes must be less than or equal 1000\"]",
                        result.getResponse().getContentAsString()));
    }

    @Test
    void findGameByIdTest() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.CREATED);

        Mockito.when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/games/" + game.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(GameStatus.CREATED.toString()));
    }

    @Test
    void tryToFindGameByIdWhenGameDoesNotExistTest() throws Exception {
        Long gameId = 1L;

        Mockito.when(gameRepository.findById(gameId))
                .thenThrow(new NoSuchEntityException(String.format("Could not find the Game by id = %s", gameId)));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/games/" + gameId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
