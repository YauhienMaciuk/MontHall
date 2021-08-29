package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.dto.GameDto;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.repository.GameRepository;
import com.hometask.montyhall.service.GameService;
import com.hometask.montyhall.service.BoxService;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final BoxService boxService;

    public GameServiceImpl(GameRepository gameRepository,
                           BoxService boxService) {
        this.gameRepository = gameRepository;
        this.boxService = boxService;
    }

    public Game createGame(GameDto gameDto) {
        Game game = new Game();
        game.setStatus(GameStatus.CREATED);

        game = gameRepository.save(game);

        boxService.createBoxes(game, gameDto.getNumberOfBoxes(), false);
        return game;
    }

    @Override
    public Game findById(Long id) {
        return gameRepository.findById(id).orElseThrow(() ->
                new NoSuchEntityException(String.format("Could not find the Game by id = %s", id)));
    }

    @Override
    public void changeGameStatus(Long gameId, GameStatus status) {
        Game game = findById(gameId);
        game.setStatus(status);
        gameRepository.save(game);
    }
}
