package com.hometask.montyhall.repository.impl;

import com.hometask.montyhall.entity.Statistic;
import com.hometask.montyhall.repository.StatisticRepository;
import com.hometask.montyhall.repository.TestPostgresDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StatisticPostgresRepositoryTest {

    private static final NamedParameterJdbcTemplate NAMED_PARAMETER_JDBC_TEMPLATE = TestPostgresDatabase.jdbcTemplate;
    private static final int NUMBER_OF_BOXES = 3;
    private static final int NUMBER_OF_GAMES = 1000000;
    private static final double CHANGE_ORIGIN_CHOICE_WIN_PERCENTAGE = 68.3;
    private static final double STICK_TO_ORIGIN_CHOICE_WIN_PERCENTAGE = 31.7;

    private StatisticRepository statisticRepository;

    @BeforeEach
    void setUp() {
        statisticRepository = new StatisticPostgresRepository(NAMED_PARAMETER_JDBC_TEMPLATE);
    }

    @AfterEach
    void cleanUp() {
        TestPostgresDatabase.resetDb();
    }

    @Test
    void should_save_statistic() {
        Statistic statistic = Statistic.builder()
                .numberOfBoxes(NUMBER_OF_BOXES)
                .numberOfGames(NUMBER_OF_GAMES)
                .changeOriginChoiceWinPercentage(BigDecimal.valueOf(CHANGE_ORIGIN_CHOICE_WIN_PERCENTAGE))
                .stickToOriginChoiceWinPercentage(BigDecimal.valueOf(STICK_TO_ORIGIN_CHOICE_WIN_PERCENTAGE))
                .build();

        Long statisticId = statisticRepository.save(statistic);

        Optional<Statistic> actual = findStatisticById(statisticId);

        Statistic expected = statistic.toBuilder()
                .id(statisticId)
                .build();
        assertThat(actual).hasValue(expected);
    }

    @Test
    void should_find_statistic_by_number_of_boxes_and_number_of_games() {
        Statistic statistic = Statistic.builder()
                .numberOfBoxes(NUMBER_OF_BOXES)
                .numberOfGames(NUMBER_OF_GAMES)
                .changeOriginChoiceWinPercentage(BigDecimal.valueOf(CHANGE_ORIGIN_CHOICE_WIN_PERCENTAGE))
                .stickToOriginChoiceWinPercentage(BigDecimal.valueOf(STICK_TO_ORIGIN_CHOICE_WIN_PERCENTAGE))
                .build();

        Long statisticId = save(statistic);

        Optional<Statistic> actual = statisticRepository.findByNumberOfBoxesAndNumberOfGames(NUMBER_OF_BOXES,
                NUMBER_OF_GAMES);

        Statistic expected = statistic.toBuilder()
                .id(statisticId)
                .build();
        assertThat(actual).hasValue(expected);
    }

    @Test
    void should_not_find_statistic_by_number_of_boxes_and_number_of_games_when_such_statistic_does_not_exist() {
        Statistic statistic = Statistic.builder()
                .numberOfBoxes(NUMBER_OF_BOXES)
                .numberOfGames(NUMBER_OF_GAMES)
                .changeOriginChoiceWinPercentage(BigDecimal.valueOf(CHANGE_ORIGIN_CHOICE_WIN_PERCENTAGE))
                .stickToOriginChoiceWinPercentage(BigDecimal.valueOf(STICK_TO_ORIGIN_CHOICE_WIN_PERCENTAGE))
                .build();

        save(statistic);

        Optional<Statistic> actual = statisticRepository.findByNumberOfBoxesAndNumberOfGames(NUMBER_OF_BOXES + 1,
                NUMBER_OF_GAMES + 1);

        assertThat(actual).isEmpty();
    }

    private Long save(Statistic statistic) {
        Integer numberOfBoxes = statistic.getNumberOfBoxes();
        Integer numberOfGames = statistic.getNumberOfGames();
        BigDecimal changeOriginChoiceWinPercentage = statistic.getChangeOriginChoiceWinPercentage();
        BigDecimal stickToOriginChoiceWinPercentage = statistic.getStickToOriginChoiceWinPercentage();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("numberOfBoxes", numberOfBoxes)
                .addValue("numberOfGames", numberOfGames)
                .addValue("changeOriginChoiceWinPercentage", changeOriginChoiceWinPercentage)
                .addValue("stickToOriginChoiceWinPercentage", stickToOriginChoiceWinPercentage);

        String sql = "INSERT INTO statistic (boxes_number, games_number, change_origin_choice_win_percentage, stick_to_origin_choice_win_percentage) " +
                "VALUES (:numberOfBoxes, :numberOfGames, :changeOriginChoiceWinPercentage, :stickToOriginChoiceWinPercentage) RETURNING id;";

        return NAMED_PARAMETER_JDBC_TEMPLATE.queryForObject(sql, parameters, Long.class);
    }

    private Optional<Statistic> findStatisticById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        String sql = "SELECT * FROM statistic WHERE id=:id";

        return NAMED_PARAMETER_JDBC_TEMPLATE.query(sql, parameters, getStatisticRowMapper()).stream()
                .findFirst();
    }

    private RowMapper<Statistic> getStatisticRowMapper() {
        return (resultSet, i) -> {
            Long id = resultSet.getLong("id");
            Integer numberOfBoxes = resultSet.getInt("boxes_number");
            Integer numberOfGames = resultSet.getInt("games_number");
            BigDecimal changeOriginChoiceWinPercentage = resultSet.getBigDecimal("change_origin_choice_win_percentage");
            BigDecimal stickToOriginChoiceWinPercentage = resultSet.getBigDecimal("stick_to_origin_choice_win_percentage");

            return Statistic.builder()
                    .id(id)
                    .numberOfBoxes(numberOfBoxes)
                    .numberOfGames(numberOfGames)
                    .changeOriginChoiceWinPercentage(changeOriginChoiceWinPercentage)
                    .stickToOriginChoiceWinPercentage(stickToOriginChoiceWinPercentage)
                    .build();
        };
    }
}
