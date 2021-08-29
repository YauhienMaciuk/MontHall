package com.hometask.montyhall.repository;

import com.hometask.montyhall.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    Statistic findByNumberOfBoxesAndNumberOfGames(int numberOfBoxes, int numberOfGames);
}
