package com.hometask.montyhall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hometask.montyhall.dto.ChooseDecision;
import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameResult;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.repository.BoxRepository;
import com.hometask.montyhall.repository.GameRepository;
import com.hometask.montyhall.repository.GameResultRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private BoxRepository boxRepository;

    @MockBean
    private GameResultRepository gameResultRepository;

    @Test
    void createGameResultWhenChangePickedBoxIsTrueTest() throws Exception {
        ChooseDecision chooseDecision = new ChooseDecision();
        chooseDecision.setChangePickedBox(true);

        Game game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.IN_PROGRESS);

        Box pickedBox = new Box();
        pickedBox.setId(2L);
        pickedBox.setGame(game);
        pickedBox.setOpened(false);
        pickedBox.setPicked(true);
        pickedBox.setWinning(false);

        Box unopenedAndUnpickedBox = new Box();
        unopenedAndUnpickedBox.setId(3L);
        unopenedAndUnpickedBox.setGame(game);
        unopenedAndUnpickedBox.setOpened(false);
        unopenedAndUnpickedBox.setPicked(true);
        unopenedAndUnpickedBox.setWinning(true);

        GameResult gameResult = new GameResult();
        gameResult.setId(4L);
        gameResult.setGame(game);
        gameResult.setPickedBoxWasChanged(chooseDecision.getChangePickedBox());
        gameResult.setWin(unopenedAndUnpickedBox.getWinning());

        Mockito.when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
        Mockito.when(boxRepository.findByGameIdAndPicked(game.getId(), true)).thenReturn(pickedBox);
        Mockito.when(boxRepository.findByGameIdAndOpenedAndPicked(game.getId(), false, false))
                .thenReturn(unopenedAndUnpickedBox);
        Mockito.when(gameResultRepository.save(any())).thenReturn(gameResult);
        Mockito.when(gameRepository.save(any())).thenReturn(game);

        mockMvc.perform(post("/games/" + game.getId() + "/game-results")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chooseDecision)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(gameResult.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pickedBoxWasChanged")
                        .value(gameResult.getPickedBoxWasChanged()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.win").value(gameResult.getWin()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.game.id").value(gameResult.getGame().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.game.status").value(gameResult.getGame()
                        .getStatus().toString()));
    }
}
