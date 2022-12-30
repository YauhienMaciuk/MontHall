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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameResultServiceImpl implements GameResultService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameResultServiceImpl.class);

    private final GameResultRepository gameResultRepository;
    private final GameService gameService;
    private final BoxService boxService;

    public GameResultServiceImpl(
            GameResultRepository gameResultRepository,
            GameService gameService,
            BoxService boxService
    ) {
        this.gameResultRepository = gameResultRepository;
        this.gameService = gameService;
        this.boxService = boxService;
    }

    @Override
    public GameResult createGameResult(Long gameId, boolean changePickedBox) {
        LOGGER.info("Create the GameResult by gameId = {} and changePickedBox = {}", gameId, changePickedBox);
        Game game = gameService.findById(gameId);
        GameStatus gameStatus = game.getStatus();

        if (gameStatus != GameStatus.IN_PROGRESS) {
            throw new GameResultException("GameResult cannot be created when game status is " + gameStatus);
        }

        List<Box> changedBoxes = new ArrayList<>();

        boolean winning = false;

        if (changePickedBox) {
            Box rePickedBox = boxService.findUnopenedAndUnpickedBoxByGameId(gameId);
            rePickedBox = rePickedBox.toBuilder()
                    .picked(true)
                    .opened(true)
                    .build();
            winning = rePickedBox.getWinning();
            changedBoxes.add(rePickedBox);
        } else {
            Box pickedBox = boxService.findPickedBoxByGameId(gameId);
            pickedBox = pickedBox.toBuilder()
                    .opened(true)
                    .build();
            changedBoxes.add(pickedBox);
        }

        GameResult gameResult = GameResult.builder()
                .gameId(gameId)
                .pickedBoxWasChanged(changePickedBox)
                .win(winning)
                .build();

        Long gameResultId = gameResultRepository.save(gameResult);

        gameService.changeGameStatus(gameId, GameStatus.FINISHED);
        boxService.updateAll(changedBoxes);

        return gameResult.toBuilder()
                .id(gameResultId)
                .build();
    }
}
