package com.hometask.montyhall.repository.impl;

import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameResult;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.repository.GameRepository;
import com.hometask.montyhall.repository.GameResultRepository;
import com.hometask.montyhall.repository.TestPostgresDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GameResultPostgresRepositoryTest {

    private static final NamedParameterJdbcTemplate NAMED_PARAMETER_JDBC_TEMPLATE = TestPostgresDatabase.jdbcTemplate;

    private GameResultRepository gameResultRepository;
    private Long gameId;

    @BeforeEach
    void setUp() {
        gameResultRepository = new GameResultPostgresRepository(NAMED_PARAMETER_JDBC_TEMPLATE);
        GameRepository gameRepository = new GamePostgresRepository(NAMED_PARAMETER_JDBC_TEMPLATE);

        Game game = Game.builder()
                .status(GameStatus.CREATED)
                .build();
        gameId = gameRepository.save(game);
    }

    @AfterEach
    void cleanUp() {
        TestPostgresDatabase.resetDb();
    }

    @Test
    void should_save_game_result() {
        GameResult gameResult = GameResult.builder()
                .gameId(gameId)
                .pickedBoxWasChanged(false)
                .win(true)
                .build();

        Long gameResultId = gameResultRepository.save(gameResult);

        Optional<GameResult> actual = findGameResultById(gameResultId);

        GameResult expected = gameResult.toBuilder()
                .id(gameResultId)
                .build();
        assertThat(actual).hasValue(expected);
    }

    private Optional<GameResult> findGameResultById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        String sql = "SELECT * FROM game_result WHERE id=:id";

        return NAMED_PARAMETER_JDBC_TEMPLATE.query(sql, parameters, getGameResultRowMapper()).stream()
                .findFirst();
    }

    private RowMapper<GameResult> getGameResultRowMapper() {
        return (resultSet, i) -> {
            Long id = resultSet.getLong("id");
            Long gameId = resultSet.getLong("game_id");
            Boolean pickedBoxWasChanged = resultSet.getBoolean("picked_box_was_changed");
            Boolean win = resultSet.getBoolean("win");

            return GameResult.builder()
                    .id(id)
                    .gameId(gameId)
                    .pickedBoxWasChanged(pickedBoxWasChanged)
                    .win(win)
                    .build();
        };
    }

}