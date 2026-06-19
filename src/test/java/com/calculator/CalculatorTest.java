package com.calculator;

import com.calculator.cli.CalculatorCLI;
import com.calculator.model.Calculator;
import com.calculator.strategy.*;
import com.calculator.command.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    private Calculator calculator;
    private CommandInvoker invoker;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
        invoker = new CommandInvoker();
    }

    // ==========================================
    // 1. Core Model & Precision Tests
    // ==========================================

    @Test
    public void testInitialState() {
        assertEquals(new BigDecimal("0.000000"), calculator.getResult());
        assertEquals(6, calculator.getResult().scale());
    }

    @Test
    public void testRoundingHalfUp() {
        // 0.1234564 rounds to 0.123456
        calculator.setResult(new BigDecimal("0.1234564"));
        assertEquals(new BigDecimal("0.123456"), calculator.getResult());

        // 0.1234565 rounds to 0.123457
        calculator.setResult(new BigDecimal("0.1234565"));
        assertEquals(new BigDecimal("0.123457"), calculator.getResult());

        // 0.1234566 rounds to 0.123457
        calculator.setResult(new BigDecimal("0.1234566"));
        assertEquals(new BigDecimal("0.123457"), calculator.getResult());
    }

    // ==========================================
    // 2. Mathematical Strategies Tests
    // ==========================================

    @Test
    public void testAddStrategy() {
        MathOperationStrategy add = new AddStrategy();
        BigDecimal result = add.calculate(new BigDecimal("5.000000"), new BigDecimal("3.1234567"));
        // 5 + 3.1234567 = 8.1234567, round to 8.123457
        assertEquals(new BigDecimal("8.123457"), result);
    }

    @Test
    public void testSubtractStrategy() {
        MathOperationStrategy sub = new SubtractStrategy();
        BigDecimal result = sub.calculate(new BigDecimal("5.000000"), new BigDecimal("3.1234564"));
        // 5 - 3.1234564 = 1.8765436, round to 1.876544
        assertEquals(new BigDecimal("1.876544"), result);
    }

    @Test
    public void testMultiplyStrategy() {
        MathOperationStrategy mul = new MultiplyStrategy();
        BigDecimal result = mul.calculate(new BigDecimal("1.500000"), new BigDecimal("2.500000"));
        // 1.5 * 2.5 = 3.750000
        assertEquals(new BigDecimal("3.750000"), result);
    }

    @Test
    public void testDivideStrategy() {
        MathOperationStrategy div = new DivideStrategy();
        // Normal division
        BigDecimal result = div.calculate(new BigDecimal("10.000000"), new BigDecimal("3.000000"));
        // 10 / 3 = 3.3333333... rounds to 3.333333
        assertEquals(new BigDecimal("3.333333"), result);

        // Division by zero exception
        assertThrows(ArithmeticException.class, () -> {
            div.calculate(new BigDecimal("10.000000"), BigDecimal.ZERO);
        });
    }

    @Test
    public void testNegativeNumbers() {
        MathOperationStrategy add = new AddStrategy();
        BigDecimal result = add.calculate(new BigDecimal("-5.000000"), new BigDecimal("-3.000000"));
        assertEquals(new BigDecimal("-8.000000"), result);
    }

    // ==========================================
    // 3. Command Pattern (Undo/Redo & State)
    // ==========================================

    @Test
    public void testArithmeticCommandExecuteAndUndo() {
        Command addCommand = new ArithmeticCommand(calculator, new AddStrategy(), new BigDecimal("10.000000"));
        
        // Execute
        addCommand.execute();
        assertEquals(new BigDecimal("10.000000"), calculator.getResult());

        // Undo
        addCommand.undo();
        assertEquals(new BigDecimal("0.000000"), calculator.getResult());
    }

    @Test
    public void testClearCommandExecuteAndUndo() {
        // Setup initial value
        calculator.setResult(new BigDecimal("5.500000"));
        
        Command clearCommand = new ClearCommand(calculator);
        
        // Execute
        clearCommand.execute();
        assertEquals(new BigDecimal("0.000000"), calculator.getResult());

        // Undo
        clearCommand.undo();
        assertEquals(new BigDecimal("5.500000"), calculator.getResult());
    }

    @Test
    public void testCommandInvokerUndoRedoStackFlow() {
        Command cmd1 = new ArithmeticCommand(calculator, new AddStrategy(), new BigDecimal("5.000000"));
        Command cmd2 = new ArithmeticCommand(calculator, new MultiplyStrategy(), new BigDecimal("3.000000"));

        assertFalse(invoker.canUndo());
        assertFalse(invoker.canRedo());

        // Do 1: + 5
        invoker.executeCommand(cmd1);
        assertEquals(new BigDecimal("5.000000"), calculator.getResult());
        assertTrue(invoker.canUndo());
        assertFalse(invoker.canRedo());

        // Do 2: * 3
        invoker.executeCommand(cmd2);
        assertEquals(new BigDecimal("15.000000"), calculator.getResult());

        // Undo 1 (returns to 5)
        invoker.undo();
        assertEquals(new BigDecimal("5.000000"), calculator.getResult());
        assertTrue(invoker.canRedo());

        // Undo 2 (returns to 0)
        invoker.undo();
        assertEquals(new BigDecimal("0.000000"), calculator.getResult());

        // Redo 1 (returns to 5)
        invoker.redo();
        assertEquals(new BigDecimal("5.000000"), calculator.getResult());

        // Redo 2 (returns to 15)
        invoker.redo();
        assertEquals(new BigDecimal("15.000000"), calculator.getResult());
    }

    @Test
    public void testExecutingNewCommandClearsRedoHistory() {
        Command cmd1 = new ArithmeticCommand(calculator, new AddStrategy(), new BigDecimal("5.000000"));
        Command cmd2 = new ArithmeticCommand(calculator, new AddStrategy(), new BigDecimal("3.000000"));

        invoker.executeCommand(cmd1); // Result: 5
        invoker.undo(); // Result: 0, Redo size: 1

        assertTrue(invoker.canRedo());

        // Execute new command
        invoker.executeCommand(cmd2); // Result: 3
        
        // Redo stack must be cleared
        assertFalse(invoker.canRedo());
        assertThrows(IllegalStateException.class, () -> invoker.redo());
    }

    @Test
    public void testInvokerBoundaries() {
        // Undo on empty history
        assertThrows(IllegalStateException.class, () -> invoker.undo());

        // Redo on empty history
        assertThrows(IllegalStateException.class, () -> invoker.redo());
    }

    // ==========================================
    // 4. CLI Parser Integration Tests
    // ==========================================

    @Test
    public void testCLIIntegration() {
        CalculatorCLI cli = new CalculatorCLI();
        
        // Retrieve internal state changes by running inputs directly on cli.processLine
        cli.processLine("+ 10.5");
        // We verify that the calculator (which is initialized inside CLI) can be indirectly tested.
        // But since Calculator is private in CLI, we can write a test for parser exceptions:
        
        // Invalid operations
        assertThrows(IllegalArgumentException.class, () -> cli.processLine("invalid_command"));
        assertThrows(IllegalArgumentException.class, () -> cli.processLine("+"));
        assertThrows(IllegalArgumentException.class, () -> cli.processLine("+ abc"));
        assertThrows(IllegalArgumentException.class, () -> cli.processLine("mod 5"));
    }
}
