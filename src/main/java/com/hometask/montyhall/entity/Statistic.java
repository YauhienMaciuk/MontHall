package com.hometask.montyhall.entity;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder(toBuilder = true)
public class Statistic {

    Long id;
    Integer numberOfBoxes;
    Integer numberOfGames;
    BigDecimal changeOriginChoiceWinPercentage;
    BigDecimal stickToOriginChoiceWinPercentage;

}
