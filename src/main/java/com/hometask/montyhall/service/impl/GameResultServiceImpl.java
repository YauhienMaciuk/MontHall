package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameResult;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.exception.GameResultException;
import com.hometask.montyhall.repository.GameResultRepository;
import com.hometask.montyhall.service.GameResultService;
import com.hometask.montyhall.service.GameService;
import com.hometask.montyhall.service.BoxService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameResultServiceImpl implements GameResultService {

    private final GameResultRepository gameResultRepository;
    private final GameService gameService;
    private final BoxService boxService;

    public GameResultServiceImpl(GameResultRepository gameResultRepository,
                                 GameService gameService,
                                 BoxService boxService) {
        this.gameResultRepository = gameResultRepository;
        this.gameService = gameService;
        this.boxService = boxService;
    }

    @Override
    public GameResult createGameResult(Long gameId, boolean changePickedBox) {
        Game game = gameService.findById(gameId);

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new GameResultException("Before get GameResult you must pick a box!");
        }

        List<Box> changedBoxes = new ArrayList<>();
        Box pickedBox = boxService.findPickedBoxByGameId(gameId);
        boolean win = pickedBox.getWinning();
        if (changePickedBox) {
            pickedBox.setPicked(false);
            Box rePickedBox = boxService.findUnopenedAndUnpickedBoxByGameId(gameId);
            rePickedBox.setPicked(true);
            rePickedBox.setOpened(true);
            win = rePickedBox.getWinning();
            changedBoxes.add(rePickedBox);
        } else {
            pickedBox.setOpened(true);
            changedBoxes.add(pickedBox);
        }

        GameResult gameResult = new GameResult();
        gameResult.setGame(game);
        gameResult.setPickedBoxWasChanged(changePickedBox);
        gameResult.setWin(win);

        gameResult = gameResultRepository.save(gameResult);
        gameService.changeGameStatus(gameId, GameStatus.FINISHED);
        boxService.updateAll(changedBoxes);

        return gameResult;
    }
}
