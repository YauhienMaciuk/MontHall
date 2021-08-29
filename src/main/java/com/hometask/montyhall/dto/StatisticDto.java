package com.hometask.montyhall.dto;

import javax.validation.constraints.NotNull;

public class StatisticDto {
    @NotNull(message = "numberOfBoxes value must not be null")
    private int numberOfBoxes;
    @NotNull(message = "numberOfGames value must not be null")
    private int numberOfGames;

    public int getNumberOfBoxes() {
        return numberOfBoxes;
    }

    public void setNumberOfBoxes(int numberOfBoxes) {
        this.numberOfBoxes = numberOfBoxes;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }
}
