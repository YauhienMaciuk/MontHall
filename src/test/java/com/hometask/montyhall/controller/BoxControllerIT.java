package com.hometask.montyhall.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hometask.montyhall.dto.BoxDto;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.exception.NoSuchEntityException;
import com.hometask.montyhall.service.BoxService;
import com.hometask.montyhall.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoxControllerIT {

    private final BoxService boxService = mock(BoxService.class);
    private final GameService gameService = mock(GameService.class);

    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        BoxController boxController = new BoxController(boxService, gameService);

        mvc = MockMvcBuilders
                .standaloneSetup(boxController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .build();
    }

    @Test
    void should_find_all_boxes_by_game_id() throws Exception {
        long gameId = 1L;

        BoxDto box1 = new BoxDto();
        box1.setId(2L);
        box1.setOpened(false);
        box1.setPicked(false);

        BoxDto box2 = new BoxDto();
        box2.setId(3L);
        box2.setOpened(false);
        box2.setPicked(false);

        BoxDto box3 = new BoxDto();
        box3.setId(4L);
        box3.setOpened(false);
        box3.setPicked(false);

        List<BoxDto> boxes = Arrays.asList(box1, box2, box3);
        when(boxService.findBoxesDtoByGameId(gameId)).thenReturn(boxes);

        String response = mvc.perform(MockMvcRequestBuilders
                .get("/games/" + gameId + "/boxes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<BoxDto> actualBoxes = objectMapper.readValue(response, new TypeReference<>() {
        });
        assertThat(actualBoxes).contains(box1, box2, box3);

        verify(boxService).findBoxesDtoByGameId(gameId);
    }

    @Test
    void should_not_find_boxes_when_game_id_does_not_exists() throws Exception {
        long gameId = 1L;
        String errMsg = "Could not find the Game by id = " + gameId;
        String expected = "\"" + errMsg + "\"";
        when(boxService.findBoxesDtoByGameId(gameId)).thenThrow(new NoSuchEntityException(errMsg));

        String actual = mvc.perform(MockMvcRequestBuilders
                .get("/games/" + gameId + "/boxes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(expected);

        verify(boxService).findBoxesDtoByGameId(gameId);
    }

    @Test
    void should_find_all_unopened_boxes_by_game_Id() throws Exception {
        long gameId = 1L;

        BoxDto box1 = new BoxDto();
        box1.setId(2L);
        box1.setOpened(true);
        box1.setPicked(false);

        BoxDto box2 = new BoxDto();
        box2.setId(3L);
        box2.setOpened(false);
        box2.setPicked(false);

        BoxDto box3 = new BoxDto();
        box3.setId(4L);
        box3.setOpened(false);
        box3.setPicked(false);

        List<BoxDto> boxes = Arrays.asList(box1, box2, box3);
        when(boxService.findUnopenedBoxesDtoByGameId(gameId)).thenReturn(boxes);

        String response = mvc.perform(MockMvcRequestBuilders
                .get("/games/" + gameId + "/boxes?unopened=true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<BoxDto> actualBoxes = objectMapper.readValue(response, new TypeReference<>() {
        });
        assertThat(actualBoxes).contains(box1, box2, box3);

        verify(boxService).findUnopenedBoxesDtoByGameId(gameId);
    }

    @Test
    void should_pick_box() throws Exception {
        long gameId = 1L;

        BoxDto box = new BoxDto();
        long boxId = 2L;
        box.setId(boxId);
        box.setOpened(false);
        box.setPicked(true);

        when(boxService.pickBox(gameId, boxId)).thenReturn(box);

        String response = mvc.perform(MockMvcRequestBuilders
                .put("/games/" + gameId + "/boxes/" + boxId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BoxDto actualBox = objectMapper.readValue(response, BoxDto.class);
        assertThat(actualBox).isEqualTo(box);

        verify(boxService).pickBox(gameId, boxId);
        verify(gameService).changeGameStatus(gameId, GameStatus.IN_PROGRESS);
    }

    @Test
    void should_not_pick_box_when_game_does_not_exist() throws Exception {
        long gameId = 1L;
        long boxId = 2L;
        String errMsg = "Could not find boxes by gameId = " + gameId;
        String expected = "\"" + errMsg + "\"";
        when(boxService.pickBox(gameId, boxId)).thenThrow(new NoSuchEntityException(errMsg));

        String actual = mvc.perform(MockMvcRequestBuilders
                .put("/games/" + gameId + "/boxes/" + boxId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(expected);

        verify(boxService).pickBox(gameId, boxId);

        verifyNoInteractions(gameService);
    }

    @Test
    void should_not_pick_box_when_box_does_not_exist() throws Exception {
        long gameId = 1L;
        long boxId = 2L;
        String errMsg = "Could not find box by boxId = " + boxId;
        String expected = "\"" + errMsg + "\"";
        when(boxService.pickBox(gameId, boxId)).thenThrow(new NoSuchEntityException(errMsg));

        String actual = mvc.perform(MockMvcRequestBuilders
                .put("/games/" + gameId + "/boxes/" + boxId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(expected);

        verify(boxService).pickBox(gameId, boxId);

        verifyNoInteractions(gameService);
    }
}
