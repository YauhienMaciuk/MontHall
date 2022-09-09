package com.hometask.montyhall.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

public class GameDto {
    private Long id;
    @Min(value = 3, message = "numberOfBoxes must be bigger than or equal 3")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameDto gameDto = (GameDto) o;
        return numberOfBoxes == gameDto.numberOfBoxes &&
                Objects.equals(id, gameDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfBoxes);
    }

    @Override
    public String toString() {
        return "GameDto{" +
                "id=" + id +
                ", numberOfBoxes=" + numberOfBoxes +
                '}';
    }
}
