package com.calculator.strategy;

import com.calculator.model.Calculator;
import java.math.BigDecimal;

/**
 * Implementation of addition strategy.
 */
public class AddStrategy implements MathOperationStrategy {
    @Override
    public BigDecimal calculate(BigDecimal left, BigDecimal right) {
        return left.add(right).setScale(Calculator.SCALE, Calculator.ROUNDING_MODE);
    }
}
