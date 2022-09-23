package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.dto.BoxDto;
import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.repository.BoxRepository;
import com.hometask.montyhall.service.BoxService;
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
    public Box createBox(Game game, boolean winning) {
        Box box = new Box();
        box.setGame(game);
        box.setOpened(false);
        box.setPicked(false);
        box.setWinning(winning);

        box = boxRepository.save(box);
        LOGGER.info("The box was created: {}", box);

        return box;
    }

    @Override
    public List<BoxDto> findBoxesDtoByGameId(Long gameId) {
        LOGGER.info("Trying to find BoxDtos by gameId = {}", gameId);
        return findBoxesByGameId(gameId).stream()
                .map(BoxDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoxDto> findUnopenedBoxesDtoByGameId(Long gameId) {
        LOGGER.info("Trying to find unopened BoxDtos by gameId = {}", gameId);
        return findBoxesByGameId(gameId).stream()
                .filter(box -> !box.getOpened())
                .map(BoxDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public BoxDto pickBox(Long gameId, Long boxId) {
        LOGGER.info("Picking box with gameId = {} nad boxId = {}", gameId, boxId);
        List<Box> boxes = findBoxesByGameId(gameId);

        Box pickedBox = boxes.stream()
                .filter(b -> b.getId().equals(boxId))
                .findFirst()
                .orElseThrow(() -> new NoSuchEntityException("Could not find box by boxId = " + boxId));

        pickedBox.setPicked(true);

        List<Box> boxesWithoutPickedBox = boxes.stream()
                .filter(b -> !b.getId().equals(boxId))
                .collect(Collectors.toList());

        List<Box> openedBoxesExceptOne = openAllBoxesExceptOne(boxesWithoutPickedBox, pickedBox);
        openedBoxesExceptOne.add(pickedBox);

        updateAll(openedBoxesExceptOne);

        return BoxDto.of(pickedBox);
    }

    @Override
    public Box findPickedBoxByGameId(Long gameId) {
        LOGGER.info("Trying to find picked box by gameId = {}", gameId);
        Box box = boxRepository.findByGameIdAndPicked(gameId, true);

        if (box == null) {
            throw new NoSuchEntityException(String.format("Could not find the picked box with gameId = %s", gameId));
        }
        return box;
    }

    @Override
    public Box findUnopenedAndUnpickedBoxByGameId(Long gameId) {
        LOGGER.info("Trying to find unopened and unpicked box by gameId = {}", gameId);
        Box box = boxRepository.findByGameIdAndOpenedAndPicked(gameId, false, false);

        if (box == null) {
            throw new NoSuchEntityException(String.format("Could not find the unopened and unpicked box " +
                    "with gameId = %s", gameId));
        }
        return box;
    }

    @Override
    public List<Box> updateAll(List<Box> boxes) {
        LOGGER.info("Trying to update {} boxes", boxes.size());
        return boxRepository.saveAll(boxes);
    }

    @Override
    public List<Box> createBoxes(Game game, int numberOfBoxes) {
        LOGGER.info("Trying to create {} boxes with gameId = {}", numberOfBoxes, game.getId());
        List<Box> createdBoxes = new ArrayList<>();
        int winningBox = RANDOM.nextInt(numberOfBoxes);

        for (int i = 0; i < numberOfBoxes; i++) {
            Box createdBox;
            if (winningBox == i) {
                createdBox = createBox(game, true);
            } else {
                createdBox = createBox(game, false);
            }
            createdBoxes.add(createdBox);
        }
        return createdBoxes;
    }

    @Override
    public List<Box> openAllBoxesExceptOne(List<Box> boxes, Box pickedBox) {
        LOGGER.info("Opening all boxes except one. Boxes size is {}", boxes.size());
        boolean winning = pickedBox.getWinning();
        if (Boolean.TRUE.equals(winning)) {
            int numberOfUnopenedBox = RANDOM.nextInt(boxes.size());
            for (int i = 0; i < boxes.size(); i++) {
                if (i != numberOfUnopenedBox) {
                    boxes.get(i).setOpened(true);
                }
            }
        } else {
            boxes.stream()
                    .filter(box -> !box.getWinning())
                    .forEach(box -> box.setOpened(true));
        }
        return boxes;
    }

    private List<Box> findBoxesByGameId(Long gameId) {
        LOGGER.info("Trying to find box by gameId = {}", gameId);

        List<Box> boxes = boxRepository.findByGameId(gameId);

        if (boxes.isEmpty()) {
            throw new NoSuchEntityException(String.format("Could not find boxes by gameId = %s", gameId));
        }

        return boxes;
    }
}
