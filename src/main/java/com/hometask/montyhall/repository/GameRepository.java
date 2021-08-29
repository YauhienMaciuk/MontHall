package com.hometask.montyhall.repository;

import com.hometask.montyhall.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
