package com.hometask.montyhall.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Min(value = 3)
    @Max(value = 1000)
    private Integer numberOfBoxes;
    @NotNull
    @Min(value = 1)
    @Max(value = 1000000)
    private Integer numberOfGames;
    @NotNull
    private BigDecimal changeOriginChoiceWinPercentage;
    @NotNull
    private BigDecimal stickToOriginChoiceWinPercentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberOfBoxes() {
        return numberOfBoxes;
    }

    public void setNumberOfBoxes(Integer numberOfBoxes) {
        this.numberOfBoxes = numberOfBoxes;
    }

    public Integer getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(Integer numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public BigDecimal getChangeOriginChoiceWinPercentage() {
        return changeOriginChoiceWinPercentage;
    }

    public void setChangeOriginChoiceWinPercentage(BigDecimal winPercentageAfterChangeOriginChoice) {
        this.changeOriginChoiceWinPercentage = winPercentageAfterChangeOriginChoice;
    }

    public BigDecimal getStickToOriginChoiceWinPercentage() {
        return stickToOriginChoiceWinPercentage;
    }

    public void setStickToOriginChoiceWinPercentage(BigDecimal winPercentageAfterStickToOriginChoice) {
        this.stickToOriginChoiceWinPercentage = winPercentageAfterStickToOriginChoice;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "id=" + id +
                ", numberOfBoxes=" + numberOfBoxes +
                ", numberOfGames=" + numberOfGames +
                ", changeOriginChoiceWinPercentage=" + changeOriginChoiceWinPercentage +
                ", stickToOriginChoiceWinPercentage=" + stickToOriginChoiceWinPercentage +
                '}';
    }
}
