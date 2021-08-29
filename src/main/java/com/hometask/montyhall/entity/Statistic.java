package com.hometask.montyhall.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer numberOfBoxes;
    private Integer numberOfGames;
    private BigDecimal changeOriginChoiceWinPercentage;
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
}
