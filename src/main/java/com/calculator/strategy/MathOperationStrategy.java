package com.calculator.strategy;

import java.math.BigDecimal;

/**
 * Strategy interface for mathematical operations.
 */
public interface MathOperationStrategy {
    /**
     * Executes the specific arithmetic calculation.
     *
     * @param left  the left operand (current result)
     * @param right the right operand (new user input)
     * @return the calculation result
     */
    BigDecimal calculate(BigDecimal left, BigDecimal right);
}
