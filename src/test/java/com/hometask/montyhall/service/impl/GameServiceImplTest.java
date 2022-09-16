package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.dto.GameDto;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.repository.GameRepository;
import com.hometask.montyhall.service.BoxService;
import com.hometask.montyhall.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    private static final long GAME_ID = 1l;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private BoxService boxService;

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameServiceImpl(gameRepository, boxService);
    }

    @Test
    void should_create_game() {
        Game game = new Game();
        game.setStatus(GameStatus.CREATED);
        GameDto gameDto = new GameDto();
        gameDto.setNumberOfBoxes(3);

        when(gameRepository.save(game)).thenReturn(game);

        Game actual = gameService.createGame(gameDto);

        assertThat(actual).isEqualTo(game);

        verify(gameRepository).save(game);
        verify(boxService).createBoxes(game, gameDto.getNumberOfBoxes());
    }

    @Test
    void should_find_game_by_id() {
        Game game = new Game();
        game.setId(GAME_ID);
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        Game actual = gameService.findById(GAME_ID);

        assertThat(actual).isEqualTo(game);

        verify(gameRepository).findById(GAME_ID);
    }

    @Test
    void should_throw_when_trying_to_find_game_by_id_which_does_not_exist() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                gameService.findById(GAME_ID)
        ).withMessage("Could not find the Game by id = " + GAME_ID);

        verify(gameRepository).findById(GAME_ID);
    }

    @Test
    void should_change_game_status() {
        Game game = new Game();
        game.setId(GAME_ID);
        game.setStatus(GameStatus.IN_PROGRESS);
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        gameService.changeGameStatus(GAME_ID, GameStatus.IN_PROGRESS);

        verify(gameRepository).findById(GAME_ID);
        verify(gameRepository).save(game);
    }

    @Test
    void should_throw_when_trying_to_change_game_status_when_game_does_not_exist() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                gameService.changeGameStatus(GAME_ID, GameStatus.IN_PROGRESS)
        ).withMessage("Could not find the Game by id = " + GAME_ID);

        verify(gameRepository).findById(GAME_ID);
        verify(gameRepository, never()).save(any());
    }
}
