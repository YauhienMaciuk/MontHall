package com.hometask.montyhall.repository.impl;

import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.repository.GameRepository;
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

public class GamePostgresRepositoryTest {

    private static final NamedParameterJdbcTemplate NAMED_PARAMETER_JDBC_TEMPLATE = TestPostgresDatabase.jdbcTemplate;

    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        gameRepository = new GamePostgresRepository(NAMED_PARAMETER_JDBC_TEMPLATE);
    }

    @AfterEach
    void cleanUp() {
        TestPostgresDatabase.resetDb();
    }

    @Test
    void should_save_game() {
        Game game = Game.builder()
                .status(GameStatus.CREATED)
                .build();

        Long gameId = gameRepository.save(game);
        Optional<Game> actual = findGame(gameId);

        Game expected = game.toBuilder()
                .id(gameId)
                .build();
        assertThat(actual).hasValue(expected);
    }

    @Test
    void should_update_game_status() {
        Game game = Game.builder()
                .status(GameStatus.CREATED)
                .build();
        Long id = saveGame(game);
        GameStatus newStatus = GameStatus.IN_PROGRESS;

        gameRepository.updateStatus(id, newStatus.name());
        Optional<Game> actual = findGame(id);

        Game expected = game.toBuilder()
                .id(id)
                .status(newStatus)
                .build();
        assertThat(actual).hasValue(expected);
    }

    @Test
    void should_find_game_by_id() {
        Game game = Game.builder()
                .status(GameStatus.CREATED)
                .build();
        Long id = saveGame(game);

        Optional<Game> actual = gameRepository.findById(id);

        Game expected = game.toBuilder()
                .id(id)
                .build();
        assertThat(actual).hasValue(expected);
    }

    private Long saveGame(Game game) {
        String status = game.getStatus().name();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("status", status);

        String sql = "INSERT INTO game (status) VALUES (:status) RETURNING id;";

        return NAMED_PARAMETER_JDBC_TEMPLATE.queryForObject(sql, parameters, Long.class);
    }

    private Optional<Game> findGame(Long gameId) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", gameId);

        String sql = "SELECT * FROM game WHERE id=:id";

        return NAMED_PARAMETER_JDBC_TEMPLATE.query(sql, parameters, getGameRowMapper()).stream()
                .findFirst();
    }

    private RowMapper<Game> getGameRowMapper() {
        return (resultSet, i) -> {
            Long id = resultSet.getLong("id");
            String statusValue = resultSet.getString("status");
            GameStatus status = GameStatus.valueOf(statusValue);

            return Game.builder()
                    .id(id)
                    .status(status)
                    .build();
        };
    }
}
