package com.hometask.montyhall.service.impl;

import com.hometask.montyhall.dto.BoxDto;
import com.hometask.montyhall.entity.Box;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoxServiceImplTest {

    private static final long GAME_ID = 1L;

    @Mock
    private BoxRepository boxRepository;

    private BoxService boxService;

    @BeforeEach
    void setUp() {
        boxService = new BoxServiceImpl(boxRepository);
    }

    @Test
    void should_create_box() {
        Long boxId = 2L;
        boolean winning = true;

        Box box = Box.builder()
                .gameId(GAME_ID)
                .opened(false)
                .picked(false)
                .winning(winning)
                .build();

        when(boxRepository.save(box)).thenReturn(boxId);

        Box actual = boxService.createBox(GAME_ID, winning);

        //todo move verify to the button of the method
        verify(boxRepository).save(box);
        box = box.toBuilder()
                .id(boxId)
                .build();
        assertThat(actual).isEqualTo(box);

    }

    @Test
    void should_find_boxes_dto_by_game_id() {
        long idOfOpenedBox = 2L;
        Box box1 = Box.builder()
                .id(idOfOpenedBox)
                .gameId(GAME_ID)
                .opened(true)
                .picked(true)
                .build();

        long idOfUnopenedBox = 3L;
        Box box2 = Box.builder()
                .id(idOfUnopenedBox)
                .gameId(GAME_ID)
                .opened(false)
                .picked(false)
                .build();

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
        Box box1 = Box.builder()
                .id(2L)
                .opened(true)
                .picked(true)
                .build();

        long unopenedBoxId = 3L;
        Box box2 = Box.builder()
                .id(unopenedBoxId)
                .opened(false)
                .picked(false)
                .build();

        List<Box> boxes = Arrays.asList(box1, box2);

        BoxDto boxDto = new BoxDto();
        boxDto.setId(unopenedBoxId);
        boxDto.setOpened(false);
        boxDto.setPicked(false);

        List<BoxDto> expected = Collections.singletonList(boxDto);

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
        long box1Id = 2L;

        Box box1 = Box.builder()
                .id(box1Id)
                .opened(false)
                .picked(false)
                .winning(true)
                .build();

        long unopenedBoxId = 3L;
        Box box2 = Box.builder()
                .id(unopenedBoxId)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        Box box3 = Box.builder()
                .id(4L)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        List<Box> boxes = Arrays.asList(box1, box2, box3);

        BoxDto expected = BoxDto.of(box1);
        expected.setPicked(true);

        when(boxRepository.findByGameId(GAME_ID)).thenReturn(boxes);

        BoxDto actual = boxService.pickBox(GAME_ID, box1Id);

        assertThat(actual).isEqualTo(expected);

        verify(boxRepository).findByGameId(GAME_ID);
        verify(boxRepository).updateAll(any());
    }

    @Test
    void should_not_pick_box_when_box_does_not_exist() {
        long idOfNotExistingBox = 0L;

        Box box1 = Box.builder()
                .id(2L)
                .opened(false)
                .picked(false)
                .winning(true)
                .build();

        long unopenedBoxId = 3L;
        Box box2 = Box.builder()
                .id(unopenedBoxId)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        Box box3 = Box.builder()
                .id(4L)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        List<Box> boxes = Arrays.asList(box1, box2, box3);

        BoxDto expected = BoxDto.of(box1);
        expected.setPicked(true);

        when(boxRepository.findByGameId(GAME_ID)).thenReturn(boxes);

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                boxService.pickBox(GAME_ID, idOfNotExistingBox)
        ).withMessage("Could not find box by boxId = " + idOfNotExistingBox);

        verify(boxRepository).findByGameId(GAME_ID);
        verify(boxRepository, never()).updateAll(any());
    }

    @Test
    void should_not_pick_box_when_game_does_not_exist() {
        long boxId = 2L;
        when(boxRepository.findByGameId(GAME_ID)).thenReturn(Collections.emptyList());

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                boxService.pickBox(GAME_ID, boxId)
        ).withMessage("Could not find boxes by gameId = " + GAME_ID);

        verify(boxRepository).findByGameId(GAME_ID);
    }

    @Test
    void should_find_picked_box_by_game_id() {
        Box box = Box.builder()
                .id(2L)
                .picked(true)
                .build();
        when(boxRepository.findPickedBoxByGameId(GAME_ID)).thenReturn(Optional.of(box));

        Box actual = boxService.findPickedBoxByGameId(GAME_ID);

        assertThat(actual).isEqualTo(box);

        verify(boxRepository).findPickedBoxByGameId(GAME_ID);
    }

    @Test
    void should_not_find_picked_box_by_game_id_when_it_does_not_exist() {
        when(boxRepository.findPickedBoxByGameId(GAME_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                boxService.findPickedBoxByGameId(GAME_ID)
        ).withMessage("Could not find the picked box with gameId=" + GAME_ID);

        verify(boxRepository).findPickedBoxByGameId(GAME_ID);
    }

    @Test
    void should_find_unopened_and_unpicked_box_by_game_id() {
        Box box = Box.builder()
                .id(2L)
                .opened(false)
                .picked(false)
                .build();

        when(boxRepository.findByGameId(GAME_ID)).thenReturn(List.of(box));

        Box actual = boxService.findUnopenedAndUnpickedBoxByGameId(GAME_ID);

        assertThat(actual).isEqualTo(box);

        verify(boxRepository).findByGameId(GAME_ID);
    }

    @Test
    void should_not_find_unopened_and_unpicked_box_by_game_id_when_it_does_not_exist() {
        when(boxRepository.findByGameId(GAME_ID)).thenReturn(Collections.EMPTY_LIST);

        assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() ->
                boxService.findUnopenedAndUnpickedBoxByGameId(GAME_ID)
        ).withMessage("Could not find the unopened and unpicked box with gameId=" + GAME_ID);

        verify(boxRepository).findByGameId(GAME_ID);
    }

    @Test
    void should_update_boxes() {
        Box box1 = Box.builder()
                .id(2L)
                .opened(false)
                .picked(false)
                .winning(true)
                .build();

        Box box2 = Box.builder()
                .id(3L)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        Box box3 = Box.builder()
                .id(4L)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        List<Box> boxes = Arrays.asList(box1, box2, box3);

        boxService.updateAll(boxes);

        verify(boxRepository).updateAll(boxes);
    }

    @Test
    void should_create_boxes() {
        Long firstBoxId = 2L;
        Long secondBoxId = 3L;
        Box firstBox = Box.builder()
                .gameId(GAME_ID)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        Box secondBox = Box.builder()
                .gameId(GAME_ID)
                .opened(false)
                .picked(false)
                .winning(true)
                .build();

        Box thirdBox = Box.builder()
                .gameId(GAME_ID)
                .opened(false)
                .picked(false)
                .winning(false)
                .build();

        int numberOfBoxes = 3;

        when(boxRepository.save(firstBox)).thenReturn(firstBoxId);
        when(boxRepository.save(secondBox)).thenReturn(secondBoxId);

        Box expectedFirstBox = firstBox.toBuilder()
                .id(firstBoxId)
                .build();
        Box expectedSecondBox = secondBox.toBuilder()
                .id(secondBoxId)
                .build();
        Box expectedThirdBox = thirdBox.toBuilder()
                .id(2L)
                .build();
        List<Box> expected = Arrays.asList(expectedFirstBox, expectedSecondBox, expectedThirdBox);

        List<Box> actual = boxService.createBoxes(GAME_ID, numberOfBoxes);

        assertThat(actual).containsAll(expected);

        verify(boxRepository, times(2)).save(firstBox);
        verify(boxRepository).save(secondBox);
    }
}
