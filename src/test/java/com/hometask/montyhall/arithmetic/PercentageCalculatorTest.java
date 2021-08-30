package com.hometask.montyhall.arithmetic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PercentageCalculatorTest {

    private PercentageCalculator percentageCalculator;

    @BeforeEach
    public void setUp() {
        percentageCalculator = new PercentageCalculator();
    }

    @Test
    void countTest() {
        int obtained = 5;
        int total = 10;

        BigDecimal percent = percentageCalculator.count(obtained, total);

        assertEquals("50.00", percent.toString());
    }
}
