package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.dto.BoxDto;
import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.repository.BoxRepository;
import com.hometask.montyhall.service.BoxService;
import com.hometask.montyhall.util.BoxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BoxServiceImpl implements BoxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoxServiceImpl.class);
    private static final Random RANDOM = new Random();

    private final BoxRepository boxRepository;

    public BoxServiceImpl(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }

    @Override
    public Box createBox(Long gameId, boolean winning) {
        Box box = Box.builder()
                .gameId(gameId)
                .opened(false)
                .picked(false)
                .winning(winning)
                .build();

        Long boxId = boxRepository.save(box);

        return box.toBuilder()
                .id(boxId)
                .build();
    }

    @Override
    public List<BoxDto> findBoxesDtoByGameId(Long gameId) {
        LOGGER.info("Find BoxDtos by gameId = {}", gameId);
        return findBoxesByGameId(gameId).stream()
                .map(BoxDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoxDto> findUnopenedBoxesDtoByGameId(Long gameId) {
        LOGGER.info("Find unopened BoxDtos by gameId = {}", gameId);
        return findBoxesByGameId(gameId).stream()
                .filter(box -> !box.getOpened())
                .map(BoxDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public BoxDto pickBox(Long gameId, Long boxId) {
        LOGGER.info("Pick the box with gameId = {} and boxId = {}", gameId, boxId);
        List<Box> boxes = findBoxesByGameId(gameId);

        Box pickedBox = boxes.stream()
                .filter(b -> b.getId().equals(boxId))
                .findFirst()
                .orElseThrow(() -> new NoSuchEntityException("Could not find box by boxId = " + boxId));

        pickedBox = pickedBox.toBuilder()
                .picked(true)
                .build();

        List<Box> boxesWithoutPickedBox = boxes.stream()
                .filter(b -> !b.getId().equals(boxId))
                .collect(Collectors.toList());

        List<Box> openedBoxesExceptOne = Boolean.TRUE.equals(pickedBox.getWinning()) ?
                BoxUtil.openAllBoxesExceptRandomOne(boxesWithoutPickedBox) : BoxUtil.openAllBoxesExceptWinningOne(boxesWithoutPickedBox);

        openedBoxesExceptOne.add(pickedBox);

        updateAll(openedBoxesExceptOne);

        return BoxDto.of(pickedBox);
    }

    @Override
    public Box findPickedBoxByGameId(Long gameId) {
        LOGGER.info("Find the picked box by gameId = {}", gameId);
        return boxRepository.findPickedBoxByGameId(gameId).orElseThrow(
                () -> new NoSuchEntityException("Could not find the picked box with gameId=" + gameId)
        );
    }

    @Override
    public Box findUnopenedAndUnpickedBoxByGameId(Long gameId) {
        LOGGER.info("Find the unopened and unpicked box by gameId = {}", gameId);

        List<Box> boxes = boxRepository.findByGameId(gameId);

        return boxes.stream()
                .filter(box -> box.getOpened() == Boolean.FALSE && box.getPicked() == Boolean.FALSE)
                .findAny()
                .orElseThrow(
                        () -> new NoSuchEntityException("Could not find the unopened and unpicked box with gameId=" + gameId)
                );
    }

    @Override
    public void updateAll(List<Box> boxes) {
        LOGGER.info("Update {} boxes", boxes.size());
        boxRepository.updateAll(boxes);
    }

    @Override
    public List<Box> createBoxes(Long gameId, int numberOfBoxes) {
        LOGGER.info("Create {} boxes with gameId = {}", numberOfBoxes, gameId);
        List<Box> createdBoxes = new ArrayList<>();
        int winningBox = RANDOM.nextInt(numberOfBoxes);

        for (int i = 0; i < numberOfBoxes; i++) {
            Box createdBox;
            if (winningBox == i) {
                createdBox = createBox(gameId, true);
            } else {
                createdBox = createBox(gameId, false);
            }
            createdBoxes.add(createdBox);
        }
        return createdBoxes;
    }

    private List<Box> findBoxesByGameId(Long gameId) {
        LOGGER.info("Find the box by gameId = {}", gameId);

        List<Box> boxes = boxRepository.findByGameId(gameId);

        if (boxes.isEmpty()) {
            throw new NoSuchEntityException(String.format("Could not find boxes by gameId = %s", gameId));
        }

        return boxes;
    }
}
