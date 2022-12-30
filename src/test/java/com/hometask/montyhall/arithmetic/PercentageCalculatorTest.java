package com.hometask.montyhall.arithmetic;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class PercentageCalculatorTest {

    private final PercentageCalculator percentageCalculator = new PercentageCalculator();

    @Test
    void should_count_percentage() {
        int obtained = 5;
        int total = 10;
        BigDecimal expected = BigDecimal.valueOf(50.00).setScale(2, RoundingMode.HALF_UP);

        BigDecimal percent = percentageCalculator.count(obtained, total);

        assertThat(percent).isEqualTo(expected);
    }
}
