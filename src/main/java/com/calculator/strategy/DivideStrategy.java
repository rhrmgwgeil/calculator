package com.calculator.strategy;

import com.calculator.model.Calculator;
import java.math.BigDecimal;

/**
 * Implementation of division strategy.
 */
public class DivideStrategy implements MathOperationStrategy {
    @Override
    public BigDecimal calculate(BigDecimal left, BigDecimal right) {
        if (right.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("錯誤：除數不能為零！");
        }
        return left.divide(right, Calculator.SCALE, Calculator.ROUNDING_MODE);
    }
}
