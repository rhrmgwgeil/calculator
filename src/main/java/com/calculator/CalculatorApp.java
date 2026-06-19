package com.calculator;

import com.calculator.cli.CalculatorCLI;

/**
 * Entry point class for the interactive calculator application.
 */
public class CalculatorApp {
    public static void main(String[] args) {
        CalculatorCLI cli = new CalculatorCLI();
        cli.run();
    }
}
