package com.hometask.montyhall.repository;

import com.hometask.montyhall.entity.Game;

import java.util.Optional;

public interface GameRepository {

    Long save(Game game);

    void updateStatus(Long gameId, String gameStatus);

    Optional<Game> findById(Long id);
}
