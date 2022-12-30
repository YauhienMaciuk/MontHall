package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.dto.GameDto;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.repository.GameRepository;
import com.hometask.montyhall.service.BoxService;
import com.hometask.montyhall.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameRepository gameRepository;
    private final BoxService boxService;

    public GameServiceImpl(
            GameRepository gameRepository,
            BoxService boxService
    ) {
        this.gameRepository = gameRepository;
        this.boxService = boxService;
    }

    @Override
    public Game createGame(GameDto gameDto) {
        LOGGER.info("Create the Game by gameDto = {}", gameDto);

        Game game = Game.builder()
                .status(GameStatus.CREATED)
                .build();

        Long gameId = gameRepository.save(game);
        int numberOfBoxes = gameDto.getNumberOfBoxes();
        boxService.createBoxes(gameId, numberOfBoxes);

        return game.toBuilder()
                .id(gameId)
                .build();
    }

    @Override
    public Game findById(Long id) {
        return gameRepository.findById(id).orElseThrow(
                () -> new NoSuchEntityException(String.format("Could not find the Game by id = %s", id))
        );
    }

    @Override
    public void changeGameStatus(Long gameId, GameStatus gameStatus) {
        String status = gameStatus.name();
        gameRepository.updateStatus(gameId, status);
    }
}
