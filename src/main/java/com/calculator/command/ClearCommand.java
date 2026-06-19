package com.calculator.command;

import com.calculator.model.Calculator;
import java.math.BigDecimal;

/**
 * Implementation of clear command that resets the result to 0.000000.
 */
public class ClearCommand implements Command {
    private final Calculator calculator;
    private BigDecimal previousValue;

    public ClearCommand(Calculator calculator) {
        if (calculator == null) {
            throw new IllegalArgumentException("Calculator cannot be null");
        }
        this.calculator = calculator;
    }

    @Override
    public void execute() {
        this.previousValue = calculator.getResult();
        calculator.setResult(BigDecimal.ZERO);
    }

    @Override
    public void undo() {
        if (previousValue == null) {
            throw new IllegalStateException("Cannot undo a command that has not been executed yet");
        }
        calculator.setResult(previousValue);
    }
}
