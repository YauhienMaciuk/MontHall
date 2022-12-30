package com.hometask.montyhall.repository.impl;

import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.repository.BoxRepository;
import com.hometask.montyhall.repository.GameRepository;
import com.hometask.montyhall.repository.TestPostgresDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class BoxPostgresRepositoryTest {

    private static final NamedParameterJdbcTemplate NAMED_PARAMETER_JDBC_TEMPLATE = TestPostgresDatabase.jdbcTemplate;

    private BoxRepository boxRepository;
    private Long gameId;

    @BeforeEach
    void setUp() {
        boxRepository = new BoxPostgresRepository(NAMED_PARAMETER_JDBC_TEMPLATE);
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
    void should_save_box() {
        Box box = Box.builder()
                .gameId(gameId)
                .winning(false)
                .picked(false)
                .opened(true)
                .build();

        Long actualBoxId = boxRepository.save(box);

        List<Box> actualBoxes = findBoxesByGameId(gameId);

        Box expectedBox = box.toBuilder()
                .id(actualBoxId)
                .build();
        assertThat(actualBoxes.size()).isEqualTo(1);
        assertThat(actualBoxes.get(0)).isEqualTo(expectedBox);
    }

    @Test
    void should_find_boxes_by_game_id() {
        Box box1 = Box.builder()
                .gameId(gameId)
                .winning(false)
                .picked(false)
                .opened(true)
                .build();
        Long idBox1 = saveBox(box1);
        box1 = box1.toBuilder()
                .id(idBox1)
                .build();

        Box box2 = Box.builder()
                .gameId(gameId)
                .winning(false)
                .picked(true)
                .opened(false)
                .build();
        Long idBox2 = saveBox(box2);
        box2 = box2.toBuilder()
                .id(idBox2)
                .build();

        Box box3 = Box.builder()
                .gameId(gameId)
                .winning(true)
                .picked(false)
                .opened(false)
                .build();
        Long idBox3 = saveBox(box3);
        box3 = box3.toBuilder()
                .id(idBox3)
                .build();

        List<Box> boxesFromDatabase = boxRepository.findByGameId(gameId);

        assertThat(boxesFromDatabase.get(0)).isEqualTo(box1);
        assertThat(boxesFromDatabase.get(1)).isEqualTo(box2);
        assertThat(boxesFromDatabase.get(2)).isEqualTo(box3);
    }

    @Test
    void should_find_picked_box_by_game_id() {
        Box box1 = Box.builder()
                .gameId(gameId)
                .winning(false)
                .picked(false)
                .opened(true)
                .build();
        saveBox(box1);

        Box box2 = Box.builder()
                .gameId(gameId)
                .winning(false)
                .picked(false)
                .opened(false)
                .build();
        saveBox(box2);

        Box pickedBox = Box.builder()
                .gameId(gameId)
                .winning(true)
                .picked(true)
                .opened(false)
                .build();
        Long pickedBoxId = saveBox(pickedBox);
        pickedBox = pickedBox.toBuilder()
                .id(pickedBoxId)
                .build();

        Optional<Box> actual = boxRepository.findPickedBoxByGameId(gameId);

        assertThat(actual).isEqualTo(Optional.of(pickedBox));
    }

    @Test
    void should_update_boxes() {
        Box firstBox = Box.builder()
                .gameId(gameId)
                .winning(false)
                .picked(false)
                .opened(true)
                .build();
        Long firstBoxId = saveBox(firstBox);
        Box updatedFirstBox = firstBox.toBuilder()
                .id(firstBoxId)
                .winning(true)
                .picked(true)
                .opened(false)
                .build();

        Box secondBox = Box.builder()
                .gameId(gameId)
                .winning(false)
                .picked(true)
                .opened(false)
                .build();
        Long secondBoxId = saveBox(secondBox);
        Box updatedSecondBox = secondBox.toBuilder()
                .id(secondBoxId)
                .picked(false)
                .opened(true)
                .build();

        List<Box> updatedBoxes = List.of(updatedFirstBox, updatedSecondBox);

        boxRepository.updateAll(updatedBoxes);

        List<Box> actualUpdatedBoxes = findBoxesByGameId(gameId);

        assertThat(actualUpdatedBoxes).containsAll(updatedBoxes);
    }

    private Long saveBox(Box box) {
        Long gameId = box.getGameId();
        Boolean opened = box.getOpened();
        Boolean winning = box.getWinning();
        Boolean picked = box.getPicked();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("gameId", gameId)
                .addValue("opened", opened)
                .addValue("winning", winning)
                .addValue("picked", picked);

        String sql = "INSERT INTO box (game_id, opened, winning, picked) VALUES (:gameId, :opened, :winning, :picked) RETURNING id;";

        return NAMED_PARAMETER_JDBC_TEMPLATE.queryForObject(sql, parameters, Long.class);
    }

    private List<Box> findBoxesByGameId(Long gameId) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("gameId", gameId);

        String sql = "SELECT * FROM box WHERE game_id=:gameId";

        return NAMED_PARAMETER_JDBC_TEMPLATE.query(sql, parameters, getBoxRowMapper());
    }

    private RowMapper<Box> getBoxRowMapper() {
        return (resultSet, i) -> {
            Long boxId = resultSet.getLong("id");
            Long gameId = resultSet.getLong("game_id");
            boolean opened = resultSet.getBoolean("opened");
            boolean picked = resultSet.getBoolean("picked");
            boolean winning = resultSet.getBoolean("winning");

            return Box.builder()
                    .id(boxId)
                    .gameId(gameId)
                    .opened(opened)
                    .picked(picked)
                    .winning(winning)
                    .build();
        };
    }
}
