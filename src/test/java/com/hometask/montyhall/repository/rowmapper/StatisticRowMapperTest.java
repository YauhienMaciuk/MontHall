package com.hometask.montyhall.repository.rowmapper;

import com.hometask.montyhall.entity.Statistic;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StatisticRowMapperTest {

    private final StatisticRowMapper statisticRowMapper = new StatisticRowMapper();

    @Test
    void should_map_row_to_box() throws SQLException {
        Long id = 1L;
        int numberOfBoxes = 3;
        int numberOfGames = 10;
        BigDecimal changeOriginChoiceWinPercentage = BigDecimal.valueOf(68.3);
        BigDecimal stickToOriginChoiceWinPercentage = BigDecimal.valueOf(31.7);
        Statistic expected = Statistic.builder()
                .id(id)
                .numberOfBoxes(numberOfBoxes)
                .numberOfGames(numberOfGames)
                .changeOriginChoiceWinPercentage(changeOriginChoiceWinPercentage)
                .stickToOriginChoiceWinPercentage(stickToOriginChoiceWinPercentage)
                .build();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(id);
        when(resultSet.getInt("boxes_number")).thenReturn(numberOfBoxes);
        when(resultSet.getInt("games_number")).thenReturn(numberOfGames);
        when(resultSet.getBigDecimal("change_origin_choice_win_percentage")).thenReturn(changeOriginChoiceWinPercentage);
        when(resultSet.getBigDecimal("stick_to_origin_choice_win_percentage")).thenReturn(stickToOriginChoiceWinPercentage);

        Statistic actual = statisticRowMapper.mapRow(resultSet, 0);

        assertThat(actual).isEqualTo(expected);

        verify(resultSet).getLong("id");
        verify(resultSet).getInt("boxes_number");
        verify(resultSet).getInt("games_number");
        verify(resultSet).getBigDecimal("change_origin_choice_win_percentage");
        verify(resultSet).getBigDecimal("stick_to_origin_choice_win_percentage");
    }
}
