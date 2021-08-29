package com.hometask.montyhall.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class GameDto {
    private Long id;
    @Min(value = 3, message = "numberOfBoxes must be bigger than 0 or equal 3")
    @Max(value = 1000, message = "numberOfBoxes must be less than or equal 1000")
    private int numberOfBoxes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfBoxes() {
        return numberOfBoxes;
    }

    public void setNumberOfBoxes(int numberOfBoxes) {
        this.numberOfBoxes = numberOfBoxes;
    }
}
