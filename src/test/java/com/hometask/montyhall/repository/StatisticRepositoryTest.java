package com.hometask.montyhall.repository;

import com.hometask.montyhall.entity.Statistic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StatisticRepositoryTest {

    @Autowired
    private StatisticRepository statisticRepository;

    @Test
    void saveStatisticAndFindItByNumberOfBoxesAndNumberOfGamesTest() {
        Statistic statistic = new Statistic();
        statistic.setNumberOfBoxes(3);
        statistic.setNumberOfGames(1000000);
        statistic.setChangeOriginChoiceWinPercentage(BigDecimal.valueOf(68.3));
        statistic.setStickToOriginChoiceWinPercentage(BigDecimal.valueOf(31.7));

        statisticRepository.save(statistic);

        Statistic receivedStatisticFromDatabase = statisticRepository.findByNumberOfBoxesAndNumberOfGames(statistic.getNumberOfBoxes(),
                statistic.getNumberOfGames());

        assertEquals(statistic.getNumberOfBoxes(), receivedStatisticFromDatabase.getNumberOfBoxes());
        assertEquals(statistic.getNumberOfGames(), receivedStatisticFromDatabase.getNumberOfGames());
        assertEquals(statistic.getChangeOriginChoiceWinPercentage(),
                receivedStatisticFromDatabase.getChangeOriginChoiceWinPercentage());
        assertEquals(statistic.getStickToOriginChoiceWinPercentage(),
                receivedStatisticFromDatabase.getStickToOriginChoiceWinPercentage());
    }

    @Test
    void tryToFindStatisticByNumberOfBoxesAndNumberOfGamesWhenItDoesNotExistTest() {
        Statistic statistic = new Statistic();
        statistic.setNumberOfBoxes(3);
        statistic.setNumberOfGames(1000000);
        statistic.setChangeOriginChoiceWinPercentage(BigDecimal.valueOf(68.3));
        statistic.setStickToOriginChoiceWinPercentage(BigDecimal.valueOf(31.7));

        statisticRepository.save(statistic);

        Statistic receivedStatisticFromDatabase = statisticRepository.findByNumberOfBoxesAndNumberOfGames(10,
                999);

        assertNull(receivedStatisticFromDatabase);
    }
}
