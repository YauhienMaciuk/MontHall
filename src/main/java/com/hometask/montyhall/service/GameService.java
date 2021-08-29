package com.hometask.montyhall.service;

import com.hometask.montyhall.dto.GameDto;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;

public interface GameService {

    Game createGame(GameDto gameDto);

    Game findById(Long id);

    void changeGameStatus(Long gameId, GameStatus status);
}
