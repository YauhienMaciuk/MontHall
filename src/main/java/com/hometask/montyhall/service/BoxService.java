package com.hometask.montyhall.service;

import com.hometask.montyhall.dto.BoxDto;
import com.hometask.montyhall.entity.Box;

import java.util.List;

public interface BoxService {

    Box createBox(Long gameId, boolean winning);

    List<BoxDto> findBoxesDtoByGameId(Long gameId);

    List<BoxDto> findUnopenedBoxesDtoByGameId(Long gameId);

    BoxDto pickBox(Long gameId, Long boxId);

    Box findPickedBoxByGameId(Long gameId);

    Box findUnopenedAndUnpickedBoxByGameId(Long gameId);

    void updateAll(List<Box> boxes);

    List<Box> createBoxes(Long gameId, int numberOfBoxes);
}
