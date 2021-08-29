package com.hometask.montyhall.repository;

import com.hometask.montyhall.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
}
