package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.dto.StatisticDto;
import com.hometask.montyhall.entity.Statistic;
import com.hometask.montyhall.exception.GameResultException;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.repository.StatisticRepository;
import com.hometask.montyhall.service.StatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticServiceImplTest {

    @Mock
    private StatisticRepository statisticRepository;

    private StatisticService statisticService;

    @BeforeEach
    void setUp() {
        statisticService = new StatisticServiceImpl(statisticRepository);
    }

    @Test
    void should_find_by_number_of_boxes_and_number_of_games() {
        int numberOfBoxes = 3;
        int numberOfGames = 1;
        Statistic statistic = new Statistic();
        statistic.setNumberOfBoxes(numberOfBoxes);
        statistic.setNumberOfGames(numberOfGames);
        when(statisticRepository.findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames)).thenReturn(statistic);

        Statistic actual = statisticService.findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames);

        assertThat(actual).isEqualTo(statistic);

        verify(statisticRepository).findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames);
    }

    @Test
    void should_throw_when_trying_to_find_statistic_by_number_of_boxes_and_number_of_games() {
        int numberOfBoxes = 3;
        int numberOfGames = 1;
        Statistic statistic = new Statistic();
        statistic.setNumberOfBoxes(numberOfBoxes);
        statistic.setNumberOfGames(numberOfGames);
        String errMsg = "Could not find Statistic with numberOfBoxes = " + numberOfBoxes + " and numberOfGames = " + numberOfGames;
        when(statisticRepository.findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames)).thenReturn(null);

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                statisticService.findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames)
        ).withMessage(errMsg);

        verify(statisticRepository).findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames);
    }

    @Test
    void should_create_statistic() {
        int numberOfBoxes = 5;
        int numberOfGames = 2;

        StatisticDto statisticDto = new StatisticDto();
        statisticDto.setNumberOfBoxes(numberOfBoxes);
        statisticDto.setNumberOfGames(numberOfGames);

        BigDecimal changeOriginChoiceWinPercentage = BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP);
        BigDecimal stickToOriginChoiceWinPercentage = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        Statistic statistic = new Statistic();
        statistic.setNumberOfBoxes(numberOfBoxes);
        statistic.setNumberOfGames(numberOfGames);
        statistic.setChangeOriginChoiceWinPercentage(changeOriginChoiceWinPercentage);
        statistic.setStickToOriginChoiceWinPercentage(stickToOriginChoiceWinPercentage);

        when(statisticRepository.findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames)).thenReturn(null);
        when(statisticRepository.save(any(Statistic.class))).thenReturn(statistic);

        Statistic actual = statisticService.createStatistic(statisticDto);

        assertThat(actual.getNumberOfBoxes()).isEqualTo(numberOfBoxes);
        assertThat(actual.getNumberOfGames()).isEqualTo(numberOfGames);
        assertThat(actual.getChangeOriginChoiceWinPercentage()).isNotNull();
        assertThat(actual.getStickToOriginChoiceWinPercentage()).isNotNull();

        verify(statisticRepository).findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames);
        verify(statisticRepository).save(any(Statistic.class));
    }

    @Test
    void should_throw_when_trying_to_create_statistic_which_already_exists() {
        int numberOfBoxes = 5;
        int numberOfGames = 2;

        StatisticDto statisticDto = new StatisticDto();
        statisticDto.setNumberOfBoxes(numberOfBoxes);
        statisticDto.setNumberOfGames(numberOfGames);

        Statistic statistic = new Statistic();
        String errMsg = "The Statistic with numberOfBoxes = " + numberOfBoxes + " and numberOfGames = " + numberOfGames + " has already existed";

        when(statisticRepository.findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames)).thenReturn(statistic);

        assertThatExceptionOfType(GameResultException.class).isThrownBy(() ->
                statisticService.createStatistic(statisticDto)
        ).withMessage(errMsg);

        verify(statisticRepository).findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames);
    }
}
