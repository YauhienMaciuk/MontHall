package com.hometask.montyhall.repository.impl;

import com.hometask.montyhall.entity.Statistic;
import com.hometask.montyhall.repository.JdbcRepositoryBase;
import com.hometask.montyhall.repository.StatisticRepository;
import com.hometask.montyhall.repository.rowmapper.StatisticRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class StatisticPostgresRepository extends JdbcRepositoryBase implements StatisticRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticPostgresRepository.class);
    private static final StatisticRowMapper STATISTIC_ROW_MAPPER = new StatisticRowMapper();

    public StatisticPostgresRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Long save(Statistic statistic) {
        LOGGER.debug("Save the statistic={}", statistic);

        Integer numberOfBoxes = statistic.getNumberOfBoxes();
        Integer numberOfGames = statistic.getNumberOfGames();
        BigDecimal changeOriginChoiceWinPercentage = statistic.getChangeOriginChoiceWinPercentage();
        BigDecimal stickToOriginChoiceWinPercentage = statistic.getStickToOriginChoiceWinPercentage();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("boxes_number", numberOfBoxes)
                .addValue("games_number", numberOfGames)
                .addValue("change_origin_choice_win_percentage", changeOriginChoiceWinPercentage)
                .addValue("stick_to_origin_choice_win_percentage", stickToOriginChoiceWinPercentage);

        String insertSql = getSql("/db/queries/statistic/insert_statistic.sql");

        return jdbcTemplate.queryForObject(insertSql, parameters, Long.class);
    }

    @Override
    public Optional<Statistic> findByNumberOfBoxesAndNumberOfGames(int numberOfBoxes, int numberOfGames) {
        LOGGER.debug("Find the statistic by numberOfBoxes {} and numberOfGames {}", numberOfBoxes, numberOfGames);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("boxes_number", numberOfBoxes)
                .addValue("games_number", numberOfGames);

        String sql = getSql("/db/queries/statistic/find_statistic_by_number_of_boxes_and_games.sql");

        return jdbcTemplate.query(sql, parameters, STATISTIC_ROW_MAPPER).stream()
                .findFirst();
    }
}
