package com.hometask.montyhall.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class StatisticDto {
    @Min(value = 3, message = "numberOfBoxes must be bigger than or equal 3")
    @Max(value = 1000, message = "numberOfBoxes must be less than or equal 1000000")
    private int numberOfBoxes;
    @Min(value = 1, message = "numberOfGames must be bigger than or equal 1")
    @Max(value = 1000000, message = "numberOfGames must be less than or equal 1000000")
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

    @Override
    public String toString() {
        return "StatisticDto{" +
                "numberOfBoxes=" + numberOfBoxes +
                ", numberOfGames=" + numberOfGames +
                '}';
    }
}
