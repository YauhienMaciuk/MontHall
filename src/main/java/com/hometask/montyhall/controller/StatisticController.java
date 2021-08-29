package com.hometask.montyhall.controller;

import com.hometask.montyhall.dto.StatisticDto;
import com.hometask.montyhall.entity.Statistic;
import com.hometask.montyhall.service.StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistics")
public class StatisticController {

    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Operation(summary = "Find Statistic by numberOfBoxes and numberOfGames")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Statistic was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Statistic.class))}),
            @ApiResponse(responseCode = "404", description = "Could not find Statistic with such numberOfBoxes and " +
                    "numberOfGames", content = @Content)
    })
    @GetMapping
    public Statistic findStatistic(@RequestParam Integer numberOfBoxes, @RequestParam Integer numberOfGames) {
        return statisticService.findByNumberOfBoxesAndNumberOfGames(numberOfBoxes, numberOfGames);
    }

    @Operation(summary = "Create Statistic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The Statistic was created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Statistic.class))}),
            @ApiResponse(responseCode = "400", description = "The request body contains invalid StatisticDto",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Statistic createStatistic(@RequestBody StatisticDto statisticDto) {
        return statisticService.createStatistic(statisticDto);
    }
}
