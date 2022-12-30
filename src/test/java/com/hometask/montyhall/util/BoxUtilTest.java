package com.hometask.montyhall.util;

import com.hometask.montyhall.entity.Box;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoxUtilTest {

    @Test
    void should_open_all_boxes_except_winning_one() {
        Box box1 = Box.builder()
                .gameId(1L)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        Box box2 = Box.builder()
                .gameId(1L)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        Box winningBox = Box.builder()
                .gameId(1L)
                .opened(false)
                .picked(false)
                .winning(true)
                .build();

        List<Box> boxes = Arrays.asList(box1, box2, winningBox);

        List<Box> actual = BoxUtil.openAllBoxesExceptWinningOne(boxes);

        box1 = box1.toBuilder()
                .opened(true)
                .build();
        box2 = box2.toBuilder()
                .opened(true)
                .build();
        List<Box> expected = Arrays.asList(box1, box2, winningBox);

        assertThat(actual).containsAll(expected);
    }

    @Test
    void should_open_all_boxes_except_random_one() {
        Box box1 = Box.builder()
                .gameId(1L)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        Box box2 = Box.builder()
                .gameId(1L)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        Box box3 = Box.builder()
                .gameId(1L)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        List<Box> boxes = Arrays.asList(box1, box2, box3);

        List<Box> actual = BoxUtil.openAllBoxesExceptRandomOne(boxes);

        box1 = box1.toBuilder()
                .opened(true)
                .build();
        box2 = box2.toBuilder()
                .opened(true)
                .build();
        List<Box> expected = Arrays.asList(box1, box2, box3);

        assertThat(actual).containsAll(expected);
    }
}
