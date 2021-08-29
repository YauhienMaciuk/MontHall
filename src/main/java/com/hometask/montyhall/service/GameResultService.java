package com.hometask.montyhall.service;

import com.hometask.montyhall.entity.GameResult;

public interface GameResultService {

    GameResult createGameResult(Long gameId, boolean changePickedBox);
}
