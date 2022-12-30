package com.hometask.montyhall.repository;

import com.hometask.montyhall.entity.Statistic;

import java.util.Optional;

public interface StatisticRepository {

    Long save(Statistic statistic);

    Optional<Statistic> findByNumberOfBoxesAndNumberOfGames(int numberOfBoxes, int numberOfGames);
}
