package com.hometask.montyhall.repository.impl;

import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.repository.GameRepository;
import com.hometask.montyhall.repository.JdbcRepositoryBase;
import com.hometask.montyhall.repository.rowmapper.GameRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GamePostgresRepository extends JdbcRepositoryBase implements GameRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(GamePostgresRepository.class);
    private static final GameRowMapper GAME_ROW_MAPPER = new GameRowMapper();

    public GamePostgresRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Long save(Game game) {
        LOGGER.debug("Save the game={}", game);

        GameStatus status = game.getStatus();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("status", status.name());

        String insertSql = getSql("/db/queries/game/insert_game.sql");

        return jdbcTemplate.queryForObject(insertSql, parameters, Long.class);
    }

    @Override
    public void updateStatus(Long gameId, String status) {
        LOGGER.debug("Set status={} for the game with id={}", status, gameId);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", gameId)
                .addValue("status", status);

        String sql = getSql("/db/queries/game/update_status_for_game.sql");

        jdbcTemplate.update(sql, parameters);
    }

    @Override
    public Optional<Game> findById(Long id) {
        LOGGER.debug("Find the game by id={}", id);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        String sql = getSql("/db/queries/game/find_game_by_id.sql");

        return jdbcTemplate.query(sql, parameters, GAME_ROW_MAPPER).stream()
                .findFirst();
    }
}
