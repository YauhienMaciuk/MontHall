package com.hometask.montyhall.service;

import com.hometask.montyhall.dto.StatisticDto;
import com.hometask.montyhall.entity.Statistic;

public interface StatisticService {

    Statistic findByNumberOfBoxesAndNumberOfGames(int numberOfBoxes, int numberOfGames);

    Statistic createStatistic(StatisticDto statisticDto);
}
