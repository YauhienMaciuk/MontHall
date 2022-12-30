package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.arithmetic.PercentageCalculator;
import com.hometask.montyhall.dto.StatisticDto;
import com.hometask.montyhall.entity.Statistic;
import com.hometask.montyhall.exception.GameResultException;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.repository.StatisticRepository;
import com.hometask.montyhall.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Service
public class StatisticServiceImpl implements StatisticService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticServiceImpl.class);
    private static final Random RANDOM = new Random();

    private final StatisticRepository statisticRepository;

    public StatisticServiceImpl(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public Statistic findByNumberOfBoxesAndNumberOfGames(int numberOfBoxes, int numberOfGames) {
        LOGGER.info("Find the Statistic by numberOfBoxes = {} and numberOfGames = {}",
                numberOfBoxes, numberOfGames);

        return statisticRepository.findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames).orElseThrow(
                () -> new NoSuchEntityException(String.format("Could not find Statistic with numberOfBoxes = %s and " +
                        "numberOfGames = %s", numberOfBoxes, numberOfGames))
        );
    }

    @Override
    public Statistic createStatistic(StatisticDto statisticDto) {
        LOGGER.info("Create the Statistic by statisticDto: {}", statisticDto);
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

        BigDecimal originChoiceWinPercentage = percentageCalculator.count(changeOriginChoiceWinPercentage, numberOfGames);
        BigDecimal nameMe = percentageCalculator.count(stickToOriginChoiceWinPercentage, numberOfGames);

        Statistic statistic = Statistic.builder()
                .numberOfBoxes(numberOfBoxes)
                .numberOfGames(numberOfGames)
                .changeOriginChoiceWinPercentage(originChoiceWinPercentage)
                .stickToOriginChoiceWinPercentage(nameMe)
                .build();


        Long statisticId = statisticRepository.save(statistic);
        LOGGER.info("The Statistic was created: {}", statistic);

        return statistic.toBuilder()
                .id(statisticId)
                .build();
    }

    private void checkIfStatisticAlreadyExists(int numberOfBoxes, int numberOfGames) {
        Optional<Statistic> statistic = statisticRepository.findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames);
        if (statistic.isPresent()) {
            throw new GameResultException(String.format("The Statistic with numberOfBoxes = %s and " +
                    "numberOfGames = %s has already existed", numberOfBoxes, numberOfGames));
        }
    }

    private boolean playGameWithoutChangingPickedBox(int numberOfBoxes) {
        boolean[] boxes = new boolean[numberOfBoxes];

        int winningBox = RANDOM.nextInt(numberOfBoxes);
        boxes[winningBox] = true;

        return boxes[RANDOM.nextInt(numberOfBoxes)];
    }
}
