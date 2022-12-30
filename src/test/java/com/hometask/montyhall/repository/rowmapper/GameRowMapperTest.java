package com.hometask.montyhall.repository.rowmapper;

import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GameRowMapperTest {

    private final GameRowMapper gameRowMapper = new GameRowMapper();

    @Test
    void should_map_row_to_game() throws SQLException {
        Long id = 1L;
        GameStatus status = GameStatus.IN_PROGRESS;
        Game expected = Game.builder()
                .id(id)
                .status(status)
                .build();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(id);
        when(resultSet.getString("status")).thenReturn(status.name());

        Game actual = gameRowMapper.mapRow(resultSet, 0);

        assertThat(actual).isEqualTo(expected);

        verify(resultSet).getLong("id");
        verify(resultSet).getString("status");
    }
}
