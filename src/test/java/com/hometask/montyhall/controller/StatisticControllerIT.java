package com.hometask.montyhall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hometask.montyhall.dto.StatisticDto;
import com.hometask.montyhall.entity.Statistic;
import com.hometask.montyhall.exception.GameResultException;
import com.hometask.montyhall.service.StatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StatisticControllerIT {

    private static final int NUMBER_OF_BOXES = 3;
    private static final int NUMBER_OF_GAMES = 1000000;

    private final StatisticService statisticService = mock(StatisticService.class);

    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        StatisticController statisticController = new StatisticController(statisticService);

        mvc = MockMvcBuilders
                .standaloneSetup(statisticController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .build();
    }

    @Test
    void should_find_statistic() throws Exception {
        long statisticId = 1L;
        BigDecimal winPercentageAfterChangeOriginChoice = BigDecimal.valueOf(66.33);
        BigDecimal winPercentageAfterStickToOriginChoice = BigDecimal.valueOf(33.37);
        Statistic statistic = new Statistic();
        statistic.setId(statisticId);
        statistic.setNumberOfBoxes(NUMBER_OF_BOXES);
        statistic.setNumberOfGames(NUMBER_OF_GAMES);
        statistic.setChangeOriginChoiceWinPercentage(winPercentageAfterChangeOriginChoice);
        statistic.setStickToOriginChoiceWinPercentage(winPercentageAfterStickToOriginChoice);

        when(statisticService.findByNumberOfBoxesAndNumberOfGames(NUMBER_OF_BOXES, NUMBER_OF_GAMES))
                .thenReturn(statistic);

        String response = mvc.perform(MockMvcRequestBuilders
                .get("/statistics?numberOfBoxes=" + NUMBER_OF_BOXES + "&numberOfGames=" + NUMBER_OF_GAMES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Statistic actualStatistic = objectMapper.readValue(response, Statistic.class);
        assertThat(actualStatistic).isEqualTo(statistic);

        verify(statisticService).findByNumberOfBoxesAndNumberOfGames(NUMBER_OF_BOXES, NUMBER_OF_GAMES);
    }

    @Test
    void should_return_empty_response_when_statistic_does_not_exist() throws Exception {

        when(statisticService.findByNumberOfBoxesAndNumberOfGames(NUMBER_OF_BOXES, NUMBER_OF_GAMES)).thenReturn(null);

        String response = mvc.perform(MockMvcRequestBuilders
                .get("/statistics?numberOfBoxes=" + NUMBER_OF_BOXES + "&numberOfGames=" + NUMBER_OF_GAMES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isEmpty();

        verify(statisticService).findByNumberOfBoxesAndNumberOfGames(NUMBER_OF_BOXES, NUMBER_OF_GAMES);
    }

    @Test
    void should_create_statistic() throws Exception {
        int numberOfBoxes = 3;
        int numberOfGames = 1000000;
        StatisticDto statisticDto = new StatisticDto();
        statisticDto.setNumberOfBoxes(numberOfBoxes);
        statisticDto.setNumberOfGames(numberOfGames);

        long statisticId = 1L;
        BigDecimal winPercentageAfterChangeOriginChoice = BigDecimal.valueOf(66.33);
        BigDecimal winPercentageAfterStickToOriginChoice = BigDecimal.valueOf(33.37);
        Statistic statistic = new Statistic();
        statistic.setId(statisticId);
        statistic.setNumberOfBoxes(numberOfBoxes);
        statistic.setNumberOfGames(numberOfGames);
        statistic.setChangeOriginChoiceWinPercentage(winPercentageAfterChangeOriginChoice);
        statistic.setStickToOriginChoiceWinPercentage(winPercentageAfterStickToOriginChoice);

        when(statisticService.createStatistic(statisticDto)).thenReturn(statistic);

        String content = objectMapper.writeValueAsString(statisticDto);
        String response = mvc.perform(post("/statistics/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Statistic actualStatistic = objectMapper.readValue(response, Statistic.class);
        assertThat(actualStatistic).isEqualTo(statistic);

        verify(statisticService).createStatistic(statisticDto);
    }

    @Test
    void should_not_create_statistic_when_it_already_exists() throws Exception {
        int numberOfBoxes = 3;
        int numberOfGames = 1000000;
        StatisticDto statisticDto = new StatisticDto();
        statisticDto.setNumberOfBoxes(numberOfBoxes);
        statisticDto.setNumberOfGames(numberOfGames);
        String content = objectMapper.writeValueAsString(statisticDto);
        String errMsg = "The Statistic with numberOfBoxes = " + numberOfBoxes + " and numberOfGames = "
                + numberOfGames + " has already existed";
        String expected = "\"" + errMsg + "\"";
        when(statisticService.createStatistic(statisticDto)).thenThrow(new GameResultException(errMsg));

        String actual = mvc.perform(post("/statistics/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(expected);

        verify(statisticService).createStatistic(statisticDto);
    }
}
