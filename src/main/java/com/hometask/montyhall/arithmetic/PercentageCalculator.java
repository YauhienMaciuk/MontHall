package com.hometask.montyhall.arithmetic;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageCalculator {

    public BigDecimal count(int obtained, int total) {
        return BigDecimal.valueOf(obtained)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    }
}
