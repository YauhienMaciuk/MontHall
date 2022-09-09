package com.hometask.montyhall.controller;

import com.hometask.montyhall.dto.ChooseDecision;
import com.hometask.montyhall.entity.GameResult;
import com.hometask.montyhall.service.GameResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class GameResultController {

    private final GameResultService gameResultService;

    public GameResultController(GameResultService gameResultService) {
        this.gameResultService = gameResultService;
    }

    @Operation(summary = "Create GameResult")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The GameResult was created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameResult.class))}),
            @ApiResponse(responseCode = "400", description = "changePickedBox value must not be null",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Could not find the Game by id", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/games/{gameId}/game-results")
    public GameResult createGameResult(@PathVariable Long gameId, @RequestBody @Valid ChooseDecision chooseDecision) {
        Boolean changePickedBox = chooseDecision.getChangePickedBox();
        return gameResultService.createGameResult(gameId, changePickedBox);
    }
}
