package com.hometask.montyhall.repository;

import com.hometask.montyhall.entity.Box;

import java.util.List;
import java.util.Optional;

public interface BoxRepository {

    Long save(Box box);

    void updateAll(List<Box> boxes);

    List<Box> findByGameId(Long gameId);

    Optional<Box> findPickedBoxByGameId(Long gameId);
}
