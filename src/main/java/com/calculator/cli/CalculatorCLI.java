package com.calculator.cli;

import com.calculator.model.Calculator;
import com.calculator.command.*;
import com.calculator.strategy.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * CalculatorCLI is the interface class responsible for standard input and
 * output logic.
 * It is completely decoupled from the calculator state and command execution
 * rules.
 */
public class CalculatorCLI {
    private final Calculator calculator;
    private final CommandInvoker commandInvoker;
    private final Map<String, MathOperationStrategy> strategies;

    public CalculatorCLI() {
        this.calculator = new Calculator();
        this.commandInvoker = new CommandInvoker();
        this.strategies = new HashMap<>();

        // Register strategies for each mathematical operator
        strategies.put("+", new AddStrategy());
        strategies.put("-", new SubtractStrategy());
        strategies.put("*", new MultiplyStrategy());
        strategies.put("/", new DivideStrategy());
    }

    /**
     * Entry point to start the interactive CLI loop.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Display current result formatted to 6 decimal places
            System.out.printf("Current Result: %s%n", calculator.getResult().toPlainString());
            System.out.println("Please enter a command (e.g., + 5, - 3, * 2, / 4, undo, redo, clear, exit):");
            System.out.print("> ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                processLine(line);
            } catch (Exception e) {
                // Ensure exceptions print an error message instead of crashing the application
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println(); // Print a blank line for better spacing
        }

        scanner.close();
    }

    /**
     * Parses and processes a single line of command input.
     *
     * @param line the trimmed line of input
     */
    public void processLine(String line) {
        if (line.isEmpty()) {
            return;
        }

        // Handle system commands
        if (line.equalsIgnoreCase("undo")) {
            commandInvoker.undo();
            return;
        }

        if (line.equalsIgnoreCase("redo")) {
            commandInvoker.redo();
            return;
        }

        if (line.equalsIgnoreCase("clear")) {
            Command clearCommand = new ClearCommand(calculator);
            commandInvoker.executeCommand(clearCommand);
            return;
        }

        // Parse arithmetic commands (e.g., "+ 5.5", "* -2.3")
        String[] parts = line.split("\\s+", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException(
                    "Invalid command format. Please use [operator] [value] (e.g., + 5) or system commands (undo, redo, clear, exit)");
        }

        String operator = parts[0];
        String operandStr = parts[1];

        // Retrieve strategy from registry
        MathOperationStrategy strategy = strategies.get(operator);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown operator: " + operator);
        }

        // Parse operand value
        BigDecimal operand;
        try {
            operand = new BigDecimal(operandStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + operandStr);
        }

        // Create and execute command via the CommandInvoker
        Command command = new ArithmeticCommand(calculator, strategy, operand);
        commandInvoker.executeCommand(command);
    }
}
