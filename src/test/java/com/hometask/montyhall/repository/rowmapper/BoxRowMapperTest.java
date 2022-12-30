package com.hometask.montyhall.repository.rowmapper;

import com.hometask.montyhall.entity.Box;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BoxRowMapperTest {

    private final BoxRowMapper boxRowMapper = new BoxRowMapper();

    @Test
    void should_map_row_to_box() throws SQLException {
        Long boxId = 1L;
        Long gameId = 2L;
        Box expected = Box.builder()
                .id(boxId)
                .gameId(gameId)
                .opened(false)
                .picked(true)
                .winning(true)
                .build();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(boxId);
        when(resultSet.getLong("game_id")).thenReturn(gameId);
        when(resultSet.getBoolean("opened")).thenReturn(false);
        when(resultSet.getBoolean("picked")).thenReturn(true);
        when(resultSet.getBoolean("winning")).thenReturn(true);

        Box actual = boxRowMapper.mapRow(resultSet, 0);

        assertThat(actual).isEqualTo(expected);

        verify(resultSet).getLong("id");
        verify(resultSet).getLong("game_id");
        verify(resultSet).getBoolean("opened");
        verify(resultSet).getBoolean("picked");
        verify(resultSet).getBoolean("winning");
    }
}
