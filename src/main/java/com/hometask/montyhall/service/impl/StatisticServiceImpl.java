package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.arithmetic.PercentageCalculator;
import com.hometask.montyhall.dto.StatisticDto;
import com.hometask.montyhall.entity.Statistic;
import com.hometask.montyhall.exception.GameResultException;
import com.hometask.montyhall.repository.StatisticRepository;
import com.hometask.montyhall.service.StatisticService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    public StatisticServiceImpl(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public Statistic findByNumberOfBoxesAndNumberOfGames(int numberOfBoxes, int numberOfGames) {
        Statistic statistic = statisticRepository.findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames);

        if (statistic == null) {
            throw new GameResultException(String.format("Could not find Statistic with numberOfBoxes = %s and " +
                            "numberOfGames = %s", numberOfBoxes, numberOfGames));
        }
        return statistic;
    }

    @Override
    public Statistic createStatistic(StatisticDto statisticDto) {
        int numberOfBoxes = statisticDto.getNumberOfBoxes();
        int numberOfGames = statisticDto.getNumberOfGames();

        checkIfStatisticAlreadyExists(numberOfBoxes, numberOfGames);

        int changeOriginChoiceWinPercentage = 0;
        int stickToOriginChoiceWinPercentage = 0;

        for (int i = 0; i < numberOfGames; i++) {
            if (playGameWithoutChangingPickedBox(numberOfBoxes)) {
                stickToOriginChoiceWinPercentage++;
            } else {
                changeOriginChoiceWinPercentage++;
            }
        }

        PercentageCalculator percentageCalculator = new PercentageCalculator();

        Statistic statistic = new Statistic();
        statistic.setNumberOfBoxes(numberOfBoxes);
        statistic.setNumberOfGames(numberOfGames);

        statistic.setChangeOriginChoiceWinPercentage(percentageCalculator
                .count(changeOriginChoiceWinPercentage, numberOfGames));

        statistic.setStickToOriginChoiceWinPercentage(percentageCalculator
                .count(stickToOriginChoiceWinPercentage, numberOfGames));

        return statisticRepository.save(statistic);
    }

    private void checkIfStatisticAlreadyExists(int numberOfBoxes, int numberOfGames) {
        Statistic statistic = statisticRepository.findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames);
        if (statistic != null) {
            throw new GameResultException(String.format("The Statistic with numberOfBoxes = %s and " +
                    "numberOfGames = %s has already existed", numberOfBoxes, numberOfGames));
        }
    }

    private boolean playGameWithoutChangingPickedBox(int numberOfBoxes) {
        Random random = new Random();
        boolean[] boxes = new boolean[numberOfBoxes];

        int winningBox = random.nextInt(numberOfBoxes);
        boxes[winningBox] = true;

        return boxes[random.nextInt(numberOfBoxes)];
    }
}
