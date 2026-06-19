package com.calculator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculator is the receiver in the Command Pattern.
 * It maintains the current result state of the calculations.
 */
public class Calculator {
    public static final int SCALE = 6;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private BigDecimal result;

    public Calculator() {
        // Initialize the result to 0.000000
        this.result = BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        if (result == null) {
            throw new IllegalArgumentException("Result cannot be null");
        }
        // Ensure that any set value is formatted to 6 decimal places
        this.result = result.setScale(SCALE, ROUNDING_MODE);
    }
}
