package com.hometask.montyhall.service;

import com.hometask.montyhall.dto.BoxDto;
import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.entity.Game;

import java.util.List;

public interface BoxService {

    Box createBox(Game game, boolean winning, boolean dryRun);

    List<BoxDto> findBoxesDtoByGameId(Long gameId);

    List<BoxDto> findUnopenedBoxesDtoByGameId(Long gameId);

    BoxDto pickBox(Long gameId, Long boxId);

    Box findPickedBoxByGameId(Long gameId);

    Box findUnopenedAndUnpickedBoxByGameId(Long gameId);

    List<Box> updateAll(List<Box> boxes);

    List<Box> createBoxes(Game game, int numberOfBoxes, boolean dryRun);

    List<Box> openAllBoxesExceptOne(List<Box> boxes, Box pickedBox);
}
