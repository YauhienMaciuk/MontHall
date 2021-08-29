package com.hometask.montyhall.controller;

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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BoxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoxRepository boxRepository;

    @MockBean
    private GameRepository gameRepository;

    @Test
    void findAllBoxesByGameIdTest() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.CREATED);

        Box box1 = new Box();
        box1.setId(2L);
        box1.setGame(game);
        box1.setOpened(false);
        box1.setPicked(false);
        box1.setWinning(false);

        Box box2 = new Box();
        box2.setId(3L);
        box2.setGame(game);
        box2.setOpened(false);
        box2.setPicked(false);
        box2.setWinning(false);

        Box box3 = new Box();
        box3.setId(4L);
        box3.setGame(game);
        box3.setOpened(false);
        box3.setPicked(false);
        box3.setWinning(true);

        Mockito.when(boxRepository.findByGameId(any())).thenReturn(Arrays.asList(box1, box2, box3));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/games/" + game.getId() + "/boxes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(box1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].opened").value(box1.getOpened()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].picked").value(box1.getPicked()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(box2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].opened").value(box2.getOpened()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].picked").value(box2.getPicked()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(box3.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].opened").value(box3.getOpened()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].picked").value(box3.getPicked()));
    }

    @Test
    void tryToFindAllBoxesByGameIdWhichDoesNotExistTest() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.CREATED);

        Mockito.when(boxRepository.findByGameId(any()))
                .thenThrow(new NoSuchEntityException(String.format("Could not find the Game by id = %s",
                        game.getId())));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/games/" + game.getId() + "/boxes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllUnopenedBoxesByGameIdTest() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.CREATED);

        Box box1 = new Box();
        box1.setId(2L);
        box1.setGame(game);
        box1.setOpened(true);
        box1.setPicked(false);
        box1.setWinning(false);

        Box box2 = new Box();
        box2.setId(3L);
        box2.setGame(game);
        box2.setOpened(false);
        box2.setPicked(false);
        box2.setWinning(false);

        Box box3 = new Box();
        box3.setId(4L);
        box3.setGame(game);
        box3.setOpened(false);
        box3.setPicked(false);
        box3.setWinning(true);

        Mockito.when(boxRepository.findByGameId(any())).thenReturn(Arrays.asList(box1, box2, box3));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/games/" + game.getId() + "/boxes?unopened=true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(box2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].opened").value(box2.getOpened()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].picked").value(box2.getPicked()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(box3.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].opened").value(box3.getOpened()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].picked").value(box3.getPicked()));
    }

    @Test
    void pickBoxTest() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.CREATED);

        Box box1 = new Box();
        box1.setId(2L);
        box1.setGame(game);
        box1.setOpened(false);
        box1.setPicked(false);
        box1.setWinning(false);

        Box box2 = new Box();
        box2.setId(3L);
        box2.setGame(game);
        box2.setOpened(false);
        box2.setPicked(false);
        box2.setWinning(false);

        Box box3 = new Box();
        box3.setId(4L);
        box3.setGame(game);
        box3.setOpened(false);
        box3.setPicked(false);
        box3.setWinning(true);

        Mockito.when(boxRepository.findByGameId(any())).thenReturn(Arrays.asList(box1, box2, box3));

        box1.setPicked(true);
        Mockito.when(boxRepository.saveAll(any())).thenReturn(Arrays.asList(box1, box2, box3));
        Mockito.when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        game.setStatus(GameStatus.IN_PROGRESS);
        Mockito.when(gameRepository.save(any())).thenReturn(game);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/games/" + game.getId() + "/boxes/" + box1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(box1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.opened").value(box1.getOpened()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.picked").value(box1.getPicked()));
    }

    @Test
    void pickBoxWhenGameDoesNotExistTest() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.CREATED);

        Box box1 = new Box();
        box1.setId(2L);
        box1.setGame(game);
        box1.setOpened(false);
        box1.setPicked(false);
        box1.setWinning(false);

        Box box2 = new Box();
        box2.setId(3L);
        box2.setGame(game);
        box2.setOpened(false);
        box2.setPicked(false);
        box2.setWinning(false);

        Box box3 = new Box();
        box3.setId(4L);
        box3.setGame(game);
        box3.setOpened(false);
        box3.setPicked(false);
        box3.setWinning(true);

        Mockito.when(boxRepository.findByGameId(any())).thenReturn(Arrays.asList(box1, box2, box3));

        box1.setPicked(true);
        Mockito.when(boxRepository.saveAll(any())).thenReturn(Arrays.asList(box1, box2, box3));
        Mockito.when(gameRepository.findById(game.getId()))
                .thenThrow(new NoSuchEntityException(String.format("Could not find the Game by id = %s",
                        game.getId())));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/games/" + game.getId() + "/boxes/" + box1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void pickBoxWhenBoxesDoNotExistTest() throws Exception {
        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.CREATED);

        Box box1 = new Box();
        box1.setId(2L);
        box1.setGame(game);
        box1.setOpened(false);
        box1.setPicked(false);
        box1.setWinning(false);

        Mockito.when(boxRepository.findByGameId(any()))
                .thenThrow(new NoSuchEntityException(String.format("Could not find boxes by gameId = %s",
                        game.getId())));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/games/" + game.getId() + "/boxes/" + box1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
