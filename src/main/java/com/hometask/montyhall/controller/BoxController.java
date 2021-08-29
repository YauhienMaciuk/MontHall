package com.hometask.montyhall.controller;

import com.hometask.montyhall.dto.BoxDto;
import com.hometask.montyhall.entity.GameStatus;
import com.hometask.montyhall.service.BoxService;
import com.hometask.montyhall.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoxController {

    private final BoxService boxService;
    private final GameService gameService;

    public BoxController(BoxService boxService,
                         GameService gameService) {
        this.boxService = boxService;
        this.gameService = gameService;
    }

    @Operation(summary = "Find all boxes by gameId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All boxes by userId were found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BoxDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Could not find boxes by gameId", content = @Content)
    })
    @GetMapping("/games/{gameId}/boxes")
    public List<BoxDto> findAllBoxesByGameId(@PathVariable Long gameId,
                                             @RequestParam (required = false) Boolean unopened) {
        if (unopened != null && unopened) {
            return boxService.findUnopenedBoxesDtoByGameId(gameId);
        } else {
            return boxService.findBoxesDtoByGameId(gameId);
        }
    }

    @Operation(summary = "Pick a box")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Box was picked",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoxDto.class))}),
            @ApiResponse(responseCode = "404", description = "Could not find boxes by gameId", content = @Content),
            @ApiResponse(responseCode = "404", description = "Could not find the Game by id", content = @Content)
    })
    @PutMapping("/games/{gameId}/boxes/{boxId}")
    public BoxDto pickBox(@PathVariable Long gameId, @PathVariable Long boxId) {
        BoxDto box = boxService.pickBox(gameId, boxId);
        gameService.changeGameStatus(gameId, GameStatus.IN_PROGRESS);
        return box;
    }
}
