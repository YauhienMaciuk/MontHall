package com.hometask.montyhall.util;

import com.hometask.montyhall.entity.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public final class BoxUtil {

    private BoxUtil() {
        //todo throw a proper one
        throw new RuntimeException();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(BoxUtil.class);
    private static final Random RANDOM = new Random();

    public static List<Box> openAllBoxesExceptRandomOne(List<Box> boxes) {
        LOGGER.info("Opening all boxes except random one. Boxes size is {}", boxes.size());
        List<Box> openedBoxedButOne = new ArrayList<>();
        int numberOfUnopenedBox = RANDOM.nextInt(boxes.size());
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            if (i != numberOfUnopenedBox) {
                box = box.toBuilder()
                        .opened(true)
                        .build();
            }
            openedBoxedButOne.add(box);
        }
        return openedBoxedButOne;
    }

    public static List<Box> openAllBoxesExceptWinningOne(List<Box> boxes) {
        LOGGER.info("Opening all boxes except winning one. Boxes size is {}", boxes.size());

        return boxes.stream()
                .map(box -> {
                    //todo refactor getWinning field
                    if (Boolean.FALSE.equals(box.getWinning())) {
                        return box.toBuilder()
                                .opened(true)
                                .build();
                    }
                    return box;
                })
                .collect(Collectors.toList());
    }
}
