package com.calculator.strategy;

import com.calculator.model.Calculator;
import java.math.BigDecimal;

/**
 * Implementation of subtraction strategy.
 */
public class SubtractStrategy implements MathOperationStrategy {
    @Override
    public BigDecimal calculate(BigDecimal left, BigDecimal right) {
        return left.subtract(right).setScale(Calculator.SCALE, Calculator.ROUNDING_MODE);
    }
}
