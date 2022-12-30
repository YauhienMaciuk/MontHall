package com.hometask.montyhall.repository.impl;

import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.repository.BoxRepository;
import com.hometask.montyhall.repository.JdbcRepositoryBase;
import com.hometask.montyhall.repository.rowmapper.BoxRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BoxPostgresRepository extends JdbcRepositoryBase implements BoxRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoxPostgresRepository.class);

    private static final BoxRowMapper BOX_ROW_MAPPER = new BoxRowMapper();

    private static final String ID = "id";
    private static final String GAME_ID = "gameId";
    private static final String OPENED = "opened";
    private static final String WINNING = "winning";
    private static final String PICKED = "picked";

    public BoxPostgresRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Long save(Box box) {
        LOGGER.debug("Save the box: {}", box);

        Long gameId = box.getGameId();
        Boolean opened = box.getOpened();
        Boolean winning = box.getWinning();
        Boolean picked = box.getPicked();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(GAME_ID, gameId)
                .addValue(OPENED, opened)
                .addValue(WINNING, winning)
                .addValue(PICKED, picked);

        String insertSql = getSql("/db/queries/box/insert_box.sql");

        return jdbcTemplate.queryForObject(insertSql, parameters, Long.class);
    }

    @Override
    public void updateAll(List<Box> boxes) {
        List<SqlParameterSource> params = new ArrayList<>();

        for (Box box : boxes) {
            Long id = box.getId();
            Boolean opened = box.getOpened();
            Boolean winning = box.getWinning();
            Boolean picked = box.getPicked();

            LOGGER.debug("Update the box: {}", box);

            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue(ID, id)
                    .addValue(OPENED, opened)
                    .addValue(WINNING, winning)
                    .addValue(PICKED, picked);

            params.add(parameters);

        }

        String sql = getSql("/db/queries/box/update_boxes.sql");

        jdbcTemplate.batchUpdate(sql, params.toArray(SqlParameterSource[]::new));
    }

    @Override
    public List<Box> findByGameId(Long gameId) {
        LOGGER.debug("Find boxes by gameId={}", gameId);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(GAME_ID, gameId);

        String sql = getSql("/db/queries/box/find_box_by_game_id.sql");

        return jdbcTemplate.query(sql, parameters, BOX_ROW_MAPPER);
    }

    @Override
    public Optional<Box> findPickedBoxByGameId(Long gameId) {
        LOGGER.debug("Find box by game id: {}", gameId);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(GAME_ID, gameId);

        String sql = getSql("/db/queries/box/find_box_by_game_id_and_picked.sql");

        return jdbcTemplate.query(sql, parameters, BOX_ROW_MAPPER).stream()
                .findFirst();
    }
}
