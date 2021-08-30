package com.hometask.montyhall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hometask.montyhall.entity.*;
import com.hometask.montyhall.repository.StatisticRepository;
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

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatisticRepository statisticRepository;

    @Test
    void findStatisticTest() throws Exception {
        Statistic statistic = new Statistic();
        statistic.setId(1L);
        statistic.setNumberOfBoxes(3);
        statistic.setNumberOfGames(1000000);
        statistic.setChangeOriginChoiceWinPercentage(BigDecimal.valueOf(68.3));
        statistic.setStickToOriginChoiceWinPercentage(BigDecimal.valueOf(31.7));

        Mockito.when(statisticRepository.findByNumberOfBoxesAndNumberOfGames(statistic.getNumberOfBoxes(),
                statistic.getNumberOfGames())).thenReturn(statistic);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/statistics?numberOfBoxes=" + statistic.getNumberOfBoxes() +
                        "&numberOfGames=" + statistic.getNumberOfGames())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(statistic.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfBoxes")
                        .value(statistic.getNumberOfBoxes()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfGames")
                        .value(statistic.getNumberOfGames()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.changeOriginChoiceWinPercentage")
                        .value(statistic.getChangeOriginChoiceWinPercentage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stickToOriginChoiceWinPercentage")
                        .value(statistic.getStickToOriginChoiceWinPercentage()));
    }

    @Test
    void tryToFindStatisticWhenItDoesNotExistTest() throws Exception {
        Statistic statistic = new Statistic();
        statistic.setId(1L);
        statistic.setNumberOfBoxes(3);
        statistic.setNumberOfGames(1000000);
        statistic.setChangeOriginChoiceWinPercentage(BigDecimal.valueOf(68.3));
        statistic.setStickToOriginChoiceWinPercentage(BigDecimal.valueOf(31.7));

        Mockito.when(statisticRepository.findByNumberOfBoxesAndNumberOfGames(statistic.getNumberOfBoxes(),
                statistic.getNumberOfGames())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/statistics?numberOfBoxes=" + statistic.getNumberOfBoxes() +
                        "&numberOfGames=" + statistic.getNumberOfGames())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createStatisticTest() throws Exception {
        Statistic statistic = new Statistic();
        statistic.setId(1L);
        statistic.setNumberOfBoxes(3);
        statistic.setNumberOfGames(1000000);
        statistic.setChangeOriginChoiceWinPercentage(BigDecimal.valueOf(66.33));
        statistic.setStickToOriginChoiceWinPercentage(BigDecimal.valueOf(33.37));

        Mockito.when(statisticRepository.findByNumberOfBoxesAndNumberOfGames(statistic.getNumberOfBoxes(),
                statistic.getNumberOfGames())).thenReturn(null);
        Mockito.when(statisticRepository.save(any())).thenReturn(statistic);

        mockMvc.perform(post("/statistics/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statistic)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(statistic.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfBoxes")
                        .value(statistic.getNumberOfBoxes()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfGames")
                        .value(statistic.getNumberOfGames()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.changeOriginChoiceWinPercentage")
                        .value(statistic.getChangeOriginChoiceWinPercentage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stickToOriginChoiceWinPercentage")
                        .value(statistic.getStickToOriginChoiceWinPercentage()));
    }

    @Test
    void tryToCreateStatisticWhenItAlreadyExistsTest() throws Exception {
        Statistic statistic = new Statistic();
        statistic.setId(1L);
        statistic.setNumberOfBoxes(3);
        statistic.setNumberOfGames(1000000);
        statistic.setChangeOriginChoiceWinPercentage(BigDecimal.valueOf(66.33));
        statistic.setStickToOriginChoiceWinPercentage(BigDecimal.valueOf(33.37));

        Mockito.when(statisticRepository.findByNumberOfBoxesAndNumberOfGames(statistic.getNumberOfBoxes(),
                statistic.getNumberOfGames())).thenReturn(statistic);

        mockMvc.perform(post("/statistics/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statistic)))
                .andExpect(status().isBadRequest());
    }
}
