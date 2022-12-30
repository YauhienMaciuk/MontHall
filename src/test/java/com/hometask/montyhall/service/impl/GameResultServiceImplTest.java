package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameResult;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.exception.GameResultException;
import com.hometask.montyhall.repository.GameResultRepository;
import com.hometask.montyhall.service.BoxService;
import com.hometask.montyhall.service.GameResultService;
import com.hometask.montyhall.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.hometask.montyhall.entity.GameStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameResultServiceImplTest {

    private static final Long GAME_ID = 1L;
    private static final Long GAME_RESULT_ID = 2L;

    @Mock
    private GameResultRepository gameResultRepository;
    @Mock
    private GameService gameService;
    @Mock
    private BoxService boxService;

    private GameResultService gameResultService;

    @BeforeEach
    void setUp() {
        gameResultService = new GameResultServiceImpl(gameResultRepository, gameService, boxService);
    }

    @Test
    void should_create_game_result_without_changing_picked_box() {
        Game game = Game.builder()
                .id(GAME_ID)
                .status(IN_PROGRESS)
                .build();
        boolean changePickedBox = false;
        GameResult gameResult = GameResult.builder()
                .gameId(GAME_ID)
                .pickedBoxWasChanged(changePickedBox)
                .win(false)
                .build();
        GameResult expectedGameResult = GameResult.builder()
                .id(GAME_RESULT_ID)
                .gameId(GAME_ID)
                .pickedBoxWasChanged(changePickedBox)
                .win(false)
                .build();
        Box box = Box.builder()
                .gameId(GAME_ID)
                .opened(false)
                .picked(true)
                .winning(true)
                .build();
        when(gameService.findById(GAME_ID)).thenReturn(game);
        when(boxService.findPickedBoxByGameId(GAME_ID)).thenReturn(box);
        box = box.toBuilder()
                .opened(true)
                .build();
        List<Box> changedBoxes = Collections.singletonList(box);
        when(gameResultRepository.save(gameResult)).thenReturn(GAME_RESULT_ID);

        GameResult actual = gameResultService.createGameResult(GAME_ID, changePickedBox);

        assertThat(actual).isEqualTo(expectedGameResult);

        verify(gameService).findById(GAME_ID);
        verify(boxService).findPickedBoxByGameId(GAME_ID);
        verify(gameResultRepository).save(gameResult);
        verify(gameService).changeGameStatus(GAME_ID, GameStatus.FINISHED);
        verify(boxService).updateAll(changedBoxes);
    }

    @Test
    void should_create_game_result_and_change_picked_box() {
        Game game = Game.builder()
                .id(GAME_ID)
                .status(IN_PROGRESS)
                .build();
        boolean changePickedBox = true;
        boolean winning = true;
        GameResult gameResult = GameResult.builder()
                .gameId(GAME_ID)
                .pickedBoxWasChanged(changePickedBox)
                .win(winning)
                .build();
        GameResult expectedGameResult = GameResult.builder()
                .id(GAME_RESULT_ID)
                .gameId(GAME_ID)
                .pickedBoxWasChanged(changePickedBox)
                .win(winning)
                .build();
        Box box = Box.builder()
                .gameId(GAME_ID)
                .opened(false)
                .picked(false)
                .winning(winning)
                .build();
        when(gameService.findById(GAME_ID)).thenReturn(game);
        when(boxService.findUnopenedAndUnpickedBoxByGameId(GAME_ID)).thenReturn(box);
        box = box.toBuilder()
                .opened(true)
                .picked(true)
                .build();
        List<Box> changedBoxes = Collections.singletonList(box);
        when(gameResultRepository.save(gameResult)).thenReturn(GAME_RESULT_ID);

        GameResult actual = gameResultService.createGameResult(GAME_ID, changePickedBox);

        assertThat(actual).isEqualTo(expectedGameResult);

        verify(gameService).findById(GAME_ID);
        verify(boxService).findUnopenedAndUnpickedBoxByGameId(GAME_ID);
        verify(gameResultRepository).save(gameResult);
        verify(gameService).changeGameStatus(GAME_ID, GameStatus.FINISHED);
        verify(boxService).updateAll(changedBoxes);
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"IN_PROGRESS"}, value = GameStatus.class)
    void should_throw_when_game_status_is_not_in_progress(GameStatus status) {
        Game game = Game.builder()
                .status(status)
                .build();

        when(gameService.findById(GAME_ID)).thenReturn(game);

        assertThatExceptionOfType(GameResultException.class).isThrownBy(() ->
                gameResultService.createGameResult(GAME_ID, false)
        ).withMessage("GameResult cannot be created when game status is " + status);

        verify(gameService).findById(GAME_ID);
    }
}
