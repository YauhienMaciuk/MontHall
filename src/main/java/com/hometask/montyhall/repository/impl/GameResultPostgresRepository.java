package com.hometask.montyhall.repository.impl;

import com.hometask.montyhall.entity.GameResult;
import com.hometask.montyhall.repository.GameResultRepository;
import com.hometask.montyhall.repository.JdbcRepositoryBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class GameResultPostgresRepository extends JdbcRepositoryBase implements GameResultRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameResultPostgresRepository.class);

    public GameResultPostgresRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Long save(GameResult gameResult) {
        LOGGER.debug("Save the gameResult={}", gameResult);

        Long gameId = gameResult.getGameId();
        Boolean pickedBoxWasChanged = gameResult.getPickedBoxWasChanged();
        Boolean win = gameResult.getWin();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("gameId", gameId)
                .addValue("pickedBoxWasChanged", pickedBoxWasChanged)
                .addValue("win", win);

        String insertSql = getSql("/db/queries/gameResult/insert_game_result.sql");

        return jdbcTemplate.queryForObject(insertSql, parameters, Long.class);
    }
}
