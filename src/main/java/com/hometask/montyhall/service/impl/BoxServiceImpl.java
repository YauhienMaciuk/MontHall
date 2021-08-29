package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.dto.BoxDto;
import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.repository.BoxRepository;
import com.hometask.montyhall.service.BoxService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BoxServiceImpl implements BoxService {

    private final BoxRepository boxRepository;

    public BoxServiceImpl(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }

    @Override
    public Box createBox(Game game, boolean winning, boolean dryRun) {
        Box box = new Box();
        box.setGame(game);
        box.setOpened(false);
        box.setPicked(false);
        box.setWinning(winning);

        if (!dryRun) {
            box = boxRepository.save(box);
        }

        return box;
    }

    @Override
    public List<BoxDto> findBoxesDtoByGameId(Long gameId) {
        return findBoxesByGameId(gameId).stream()
                .map(BoxDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoxDto> findUnopenedBoxesDtoByGameId(Long gameId) {
        return findBoxesByGameId(gameId).stream()
                .filter(box -> !box.getOpened())
                .map(BoxDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public BoxDto pickBox(Long gameId, Long boxId) {
        List<Box> boxes = findBoxesByGameId(gameId);

        Box pickedBox = boxes.stream()
                .filter(b -> b.getId().equals(boxId))
                .findFirst()
                .orElseThrow(() -> new NoSuchEntityException("Could not find box by boxId"));

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
        Box box = boxRepository.findByGameIdAndPicked(gameId, true);

        if (box == null) {
            throw new NoSuchEntityException(String.format("Could not find the picked box with gameId = %s", gameId));
        }
        return box;
    }

    @Override
    public Box findUnopenedAndUnpickedBoxByGameId(Long gameId) {
        Box box = boxRepository.findByGameIdAndOpenedAndPicked(gameId, false, false);

        if (box == null) {
            throw new NoSuchEntityException(String.format("Could not find the unopened and unpicked box " +
                    "with gameId = %s", gameId));
        }
        return box;
    }

    @Override
    public List<Box> updateAll(List<Box> boxes) {
        return boxRepository.saveAll(boxes);
    }

    @Override
    public List<Box> createBoxes(Game game, int numberOfBoxes, boolean dryRun) {
        List<Box> createdBoxes = new ArrayList<>();
        Random random = new Random();
        int winningBox = random.nextInt(numberOfBoxes);

        for (int i = 0; i < numberOfBoxes; i++) {
            Box createdBox;
            if (winningBox == i) {
                createdBox = createBox(game, true, dryRun);
            } else {
                createdBox = createBox(game, false, dryRun);
            }
            createdBoxes.add(createdBox);
        }
        return createdBoxes;
    }

    @Override
    public List<Box> openAllBoxesExceptOne(List<Box> boxes, Box pickedBox) {

        if (!pickedBox.getWinning()) {
            boxes.stream()
                    .filter(box -> !box.getWinning())
                    .forEach(box -> box.setOpened(true));
        } else {
            Random random = new Random();
            int numberOfUnopenedBox = random.nextInt(boxes.size());
//            IntStream.range(0, boxes.size())

            for (int i = 0; i < boxes.size(); i++) {
                if (i != numberOfUnopenedBox) {
                    boxes.get(i).setOpened(true);
                }
            }
        }
        return boxes;
    }

    private List<Box> findBoxesByGameId(Long gameId) {
        List<Box> boxes = boxRepository.findByGameId(gameId);

        if (boxes.isEmpty()) {
            throw new NoSuchEntityException(String.format("Could not find boxes by gameId = %s", gameId));
        }

        return boxes;
    }
}
