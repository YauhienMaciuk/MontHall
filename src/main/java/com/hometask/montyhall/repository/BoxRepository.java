package com.hometask.montyhall.repository;

import com.hometask.montyhall.entity.Box;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoxRepository extends JpaRepository<Box, Long> {

    List<Box> findByGameId(Long gameId);

    Box findByGameIdAndPicked(Long gameId, Boolean picked);

    Box findByGameIdAndOpenedAndPicked(Long gameId, Boolean opened, Boolean picked);
}
