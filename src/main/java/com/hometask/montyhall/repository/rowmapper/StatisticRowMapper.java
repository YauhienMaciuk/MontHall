package com.hometask.montyhall.repository.rowmapper;

import com.hometask.montyhall.entity.Statistic;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticRowMapper implements RowMapper<Statistic> {

    @Override
    public Statistic mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("id");
        Integer numberOfBoxes = resultSet.getInt("boxes_number");
        Integer numberOfGames = resultSet.getInt("games_number");
        BigDecimal changeOriginChoiceWinPercentage = resultSet.getBigDecimal("change_origin_choice_win_percentage");
        BigDecimal stickToOriginChoiceWinPercentage = resultSet.getBigDecimal("stick_to_origin_choice_win_percentage");

        return Statistic.builder()
                .id(id)
                .numberOfBoxes(numberOfBoxes)
                .numberOfGames(numberOfGames)
                .changeOriginChoiceWinPercentage(changeOriginChoiceWinPercentage)
                .stickToOriginChoiceWinPercentage(stickToOriginChoiceWinPercentage)
                .build();
    }
}
