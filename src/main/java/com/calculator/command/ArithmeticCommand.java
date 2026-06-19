package com.calculator.command;

import com.calculator.model.Calculator;
import com.calculator.strategy.MathOperationStrategy;
import java.math.BigDecimal;

/**
 * Implementation of arithmetic command representing an arithmetic operation
 * (add, subtract, multiply, divide).
 */
public class ArithmeticCommand implements Command {
    private final Calculator calculator;
    private final MathOperationStrategy strategy;
    private final BigDecimal operand;
    private BigDecimal previousValue;

    public ArithmeticCommand(Calculator calculator, MathOperationStrategy strategy, BigDecimal operand) {
        if (calculator == null) {
            throw new IllegalArgumentException("Calculator cannot be null");
        }
        if (strategy == null) {
            throw new IllegalArgumentException("MathOperationStrategy cannot be null");
        }
        if (operand == null) {
            throw new IllegalArgumentException("Operand cannot be null");
        }
        this.calculator = calculator;
        this.strategy = strategy;
        this.operand = operand;
    }

    @Override
    public void execute() {
        // Save previous value for undoing
        this.previousValue = calculator.getResult();

        // Execute the strategy calculation (may throw ArithmeticException)
        BigDecimal nextValue = strategy.calculate(previousValue, operand);

        // Update calculator state
        calculator.setResult(nextValue);
    }

    @Override
    public void undo() {
        if (previousValue == null) {
            throw new IllegalStateException("Cannot undo a command that has not been executed yet");
        }
        calculator.setResult(previousValue);
    }
}
