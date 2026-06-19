package com.calculator.strategy;

import com.calculator.model.Calculator;
import java.math.BigDecimal;

/**
 * Implementation of multiplication strategy.
 */
public class MultiplyStrategy implements MathOperationStrategy {
    @Override
    public BigDecimal calculate(BigDecimal left, BigDecimal right) {
        return left.multiply(right).setScale(Calculator.SCALE, Calculator.ROUNDING_MODE);
    }
}
