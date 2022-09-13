package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.dto.BoxDto;
import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.repository.BoxRepository;
import com.hometask.montyhall.service.BoxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoxServiceImplTest {

    private static final long GAME_ID = 1l;

    @Mock
    private BoxRepository boxRepository;

    private BoxService boxService;

    @BeforeEach
    void setUp() {
        boxService = new BoxServiceImpl(boxRepository);
    }

    @Test
    void should_create_box() {
        Game game = new Game();
        boolean winning = true;

        Box box = new Box();
        box.setGame(game);
        box.setOpened(false);
        box.setPicked(false);
        box.setWinning(winning);

        when(boxRepository.save(box)).thenReturn(box);

        Box actual = boxService.createBox(game, winning);

        assertThat(actual).isEqualTo(box);

        verify(boxRepository).save(box);
    }

    @Test
    void should_find_boxes_dto_by_game_id() {
        Game game = new Game();
        game.setId(GAME_ID);

        Box box1 = new Box();
        long idOfOpenedBox = 2l;
        box1.setId(idOfOpenedBox);
        box1.setOpened(true);
        box1.setPicked(true);
        box1.setGame(game);

        Box box2 = new Box();
        long idOfUnopenedBox = 3l;
        box2.setId(idOfUnopenedBox);
        box2.setOpened(false);
        box2.setPicked(false);
        box2.setGame(game);

        List<Box> boxes = Arrays.asList(box1, box2);

        BoxDto boxDto1 = new BoxDto();
        boxDto1.setId(idOfOpenedBox);
        boxDto1.setOpened(true);
        boxDto1.setPicked(true);

        BoxDto boxDto2 = new BoxDto();
        boxDto2.setId(idOfUnopenedBox);
        boxDto2.setOpened(false);
        boxDto2.setPicked(false);

        List<BoxDto> expected = Arrays.asList(boxDto1, boxDto2);

        when(boxRepository.findByGameId(GAME_ID)).thenReturn(boxes);

        List<BoxDto> actual = boxService.findBoxesDtoByGameId(GAME_ID);

        assertThat(actual).containsAll(expected);

        verify(boxRepository).findByGameId(GAME_ID);
    }

    @Test
    void should_not_find_boxes_dto_by_game_id_when_such_game_does_not_exist() {
        when(boxRepository.findByGameId(GAME_ID)).thenReturn(Collections.emptyList());

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                boxService.findBoxesDtoByGameId(GAME_ID)
        ).withMessage("Could not find boxes by gameId = " + GAME_ID);

        verify(boxRepository).findByGameId(GAME_ID);
    }

    @Test
    void should_find_unopened_boxes_dto_by_game_id() {
        Box box1 = new Box();
        box1.setId(2l);
        box1.setOpened(true);
        box1.setPicked(true);

        Box box2 = new Box();
        long unopenedBoxId = 3l;
        box2.setId(unopenedBoxId);
        box2.setOpened(false);
        box2.setPicked(false);

        List<Box> boxes = Arrays.asList(box1, box2);

        BoxDto boxDto = new BoxDto();
        boxDto.setId(unopenedBoxId);
        boxDto.setOpened(false);
        boxDto.setPicked(false);

        List<BoxDto> expected = Arrays.asList(boxDto);

        when(boxRepository.findByGameId(GAME_ID)).thenReturn(boxes);

        List<BoxDto> actual = boxService.findUnopenedBoxesDtoByGameId(GAME_ID);

        assertThat(actual).isEqualTo(expected);

        verify(boxRepository).findByGameId(GAME_ID);
    }

    @Test
    void should_not_find_unopened_boxes_dto_by_game_id_when_such_game_does_not_exist() {
        when(boxRepository.findByGameId(GAME_ID)).thenReturn(Collections.emptyList());

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                boxService.findUnopenedBoxesDtoByGameId(GAME_ID)
        ).withMessage("Could not find boxes by gameId = " + GAME_ID);
    }

    @Test
    void should_pick_box() {
        long boxId = 2l;

        Box box1 = new Box();
        box1.setId(2l);
        box1.setOpened(false);
        box1.setPicked(false);
        box1.setWinning(true);

        Box box2 = new Box();
        long unopenedBoxId = 3l;
        box2.setId(unopenedBoxId);
        box2.setOpened(false);
        box2.setPicked(false);
        box2.setWinning(false);

        Box box3 = new Box();
        box3.setId(4l);
        box3.setOpened(false);
        box3.setPicked(false);
        box3.setWinning(false);

        List<Box> boxes = Arrays.asList(box1, box2, box3);

        BoxDto expected = BoxDto.of(box1);
        expected.setPicked(true);

        when(boxRepository.findByGameId(GAME_ID)).thenReturn(boxes);

        BoxDto actual = boxService.pickBox(GAME_ID, boxId);

        assertThat(actual).isEqualTo(expected);

        verify(boxRepository).findByGameId(GAME_ID);

        List<Box> savedBoxes = Arrays.asList(box2, box3, box1);
        verify(boxRepository).saveAll(savedBoxes);
    }

    @Test
    void should_not_pick_box_when_box_does_not_exist() {
        long idOfNotExistingBox = 0l;

        Box box1 = new Box();
        box1.setId(2l);
        box1.setOpened(false);
        box1.setPicked(false);
        box1.setWinning(true);

        Box box2 = new Box();
        long unopenedBoxId = 3l;
        box2.setId(unopenedBoxId);
        box2.setOpened(false);
        box2.setPicked(false);
        box2.setWinning(false);

        Box box3 = new Box();
        box3.setId(4l);
        box3.setOpened(false);
        box3.setPicked(false);
        box3.setWinning(false);

        List<Box> boxes = Arrays.asList(box1, box2, box3);

        BoxDto expected = BoxDto.of(box1);
        expected.setPicked(true);

        when(boxRepository.findByGameId(GAME_ID)).thenReturn(boxes);

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                boxService.pickBox(GAME_ID, idOfNotExistingBox)
        ).withMessage("Could not find box by boxId = " + idOfNotExistingBox);

        verify(boxRepository).findByGameId(GAME_ID);
        verify(boxRepository, never()).saveAll(any());
    }

    @Test
    void should_not_pick_box_when_game_does_not_exist() {
        long boxId = 2l;
        when(boxRepository.findByGameId(GAME_ID)).thenReturn(Collections.emptyList());

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                boxService.pickBox(GAME_ID, boxId)
        ).withMessage("Could not find boxes by gameId = " + GAME_ID);

        verify(boxRepository).findByGameId(GAME_ID);
    }

    @Test
    void should_find_picked_box_by_game_id() {
        Box box = new Box();
        long boxId = 2l;
        box.setId(boxId);
        box.setPicked(true);
        when(boxRepository.findByGameIdAndPicked(GAME_ID, true)).thenReturn(box);

        Box actual = boxService.findPickedBoxByGameId(GAME_ID);

        assertThat(actual).isEqualTo(box);

        verify(boxRepository).findByGameIdAndPicked(GAME_ID, true);
    }

    @Test
    void should_not_find_picked_box_by_game_id_when_it_does_not_exist() {
        when(boxRepository.findByGameIdAndPicked(GAME_ID, true)).thenReturn(null);

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                boxService.findPickedBoxByGameId(GAME_ID)
        ).withMessage("Could not find the picked box with gameId = " + GAME_ID);

        verify(boxRepository).findByGameIdAndPicked(GAME_ID, true);
    }

    @Test
    void should_find_unopened_and_unpicked_box_by_game_id() {
        Box box = new Box();
        long boxId = 2l;
        box.setId(boxId);
        box.setPicked(true);

        when(boxRepository.findByGameIdAndOpenedAndPicked(GAME_ID, false, false)).thenReturn(box);

        Box actual = boxService.findUnopenedAndUnpickedBoxByGameId(GAME_ID);

        assertThat(actual).isEqualTo(box);

        verify(boxRepository).findByGameIdAndOpenedAndPicked(GAME_ID, false, false);
    }

    @Test
    void should_not_find_unopened_and_unpicked_box_by_game_id_when_it_does_not_exist() {
        when(boxRepository.findByGameIdAndOpenedAndPicked(GAME_ID, false, false)).thenReturn(null);

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                boxService.findUnopenedAndUnpickedBoxByGameId(GAME_ID)
        ).withMessage("Could not find the unopened and unpicked box with gameId = " + GAME_ID);

        verify(boxRepository).findByGameIdAndOpenedAndPicked(GAME_ID, false, false);
    }

    @Test
    void should_update_boxes() {
        Box box1 = new Box();
        box1.setId(2l);
        box1.setOpened(false);
        box1.setPicked(false);
        box1.setWinning(true);

        Box box2 = new Box();
        long unopenedBoxId = 3l;
        box2.setId(unopenedBoxId);
        box2.setOpened(false);
        box2.setPicked(false);
        box2.setWinning(false);

        Box box3 = new Box();
        box3.setId(4l);
        box3.setOpened(false);
        box3.setPicked(false);
        box3.setWinning(false);

        List<Box> boxes = Arrays.asList(box1, box2, box3);

        when(boxRepository.saveAll(boxes)).thenReturn(boxes);

        List<Box> actual = boxService.updateAll(boxes);

        assertThat(actual).isEqualTo(boxes);

        verify(boxRepository).saveAll(boxes);
    }

    @Test
    void should_create_boxes() {
        Game game = new Game();
        Box box1 = new Box();
        box1.setGame(game);
        box1.setOpened(false);
        box1.setPicked(false);
        box1.setWinning(false);

        Box box2 = new Box();
        box2.setGame(game);
        box2.setOpened(false);
        box2.setPicked(false);
        box2.setWinning(true);

        Box box3 = new Box();
        box3.setGame(game);
        box3.setOpened(false);
        box3.setPicked(false);
        box3.setWinning(false);

        List<Box> expected = Arrays.asList(box1, box2, box3);
        int numberOfBoxes = 3;

        when(boxRepository.save(box1)).thenReturn(box1);
        when(boxRepository.save(box2)).thenReturn(box2);

        List<Box> actual = boxService.createBoxes(game, numberOfBoxes);

        assertThat(actual).containsAll(expected);

        verify(boxRepository, times(2)).save(box1);
        verify(boxRepository).save(box2);
    }

    @Test
    void should_open_all_boxes_except_one() {
        Game game = new Game();
        Box box1 = new Box();
        box1.setGame(game);
        box1.setOpened(false);
        box1.setPicked(false);
        box1.setWinning(false);

        Box box2 = new Box();
        box2.setGame(game);
        box2.setOpened(false);
        box2.setPicked(false);
        box2.setWinning(true);

        Box box3 = new Box();
        box3.setGame(game);
        box3.setOpened(false);
        box3.setPicked(false);
        box3.setWinning(false);

        List<Box> boxes = Arrays.asList(box1, box2, box3);

        box1.setOpened(true);
        box2.setOpened(true);
        List<Box> expected = Arrays.asList(box1, box2, box3);

        List<Box> actual = boxService.openAllBoxesExceptOne(boxes, box3);

        assertThat(actual).containsAll(expected);
    }
}
