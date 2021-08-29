package com.hometask.montyhall.controller;

import com.hometask.montyhall.dto.GameDto;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Create a Game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The Game was created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Game.class))}),
            @ApiResponse(responseCode = "400", description = "The request body contains invalid GameDto",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody @Valid GameDto gameDto) {
        return gameService.createGame(gameDto);
    }

    @Operation(summary = "Find Game by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Game was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Game.class))}),
            @ApiResponse(responseCode = "404", description = "Could not find the Game by id", content = @Content)
    })
    @GetMapping("/{id}")
    public Game findGameById(@PathVariable Long id) {
        return gameService.findById(id);
    }
}
