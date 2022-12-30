package com.hometask.montyhall.repository.rowmapper;

import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameRowMapper implements RowMapper<Game> {

    @Override
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String statusValue = rs.getString("status");
        GameStatus status = GameStatus.valueOf(statusValue);

        return Game.builder()
                .id(id)
                .status(status)
                .build();
    }
}
