package com.hometask.montyhall.repository.rowmapper;

import com.hometask.montyhall.entity.Box;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoxRowMapper implements RowMapper<Box> {

    @Override
    public Box mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long boxId = rs.getLong("id");
        Long gameId = rs.getLong("game_id");
        boolean opened = rs.getBoolean("opened");
        boolean picked = rs.getBoolean("picked");
        boolean winning = rs.getBoolean("winning");

        return Box.builder()
                .id(boxId)
                .gameId(gameId)
                .opened(opened)
                .picked(picked)
                .winning(winning)
                .build();
    }
}
