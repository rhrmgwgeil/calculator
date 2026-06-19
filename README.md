# Interactive CLI Calculator

A robust, interactive Command Line Interface (CLI) calculator built with **Java 21**. This project is designed strictly following Object-Oriented Design (OOD) guidelines and Clean Code principles, utilizing design patterns to maximize extensibility and testability.

---

## Key Features

- **Precision-Safe Calculations**: Full decimal arithmetic using Java's `BigDecimal` to avoid floating-point binary representation inaccuracies.
- **Strict Scale & Rounding**: Calculations and output formatting are locked to **6 decimal places** using **Half-Up Rounding** (`RoundingMode.HALF_UP`).
- **History Management (Undo/Redo)**: Full timeline navigation supporting undoing and redoing calculations and state resets (clear).
- **Graceful Error Handling**: Detects and reports mathematical errors (like division by zero) and invalid formatting without crashing the application.
- **Interactive Shell Loop**: An infinite loop that processes user commands until `exit` is entered.

---

## Design Patterns

This application incorporates two key behavioral design patterns to decouple state, presentation, and operations:

### 1. Strategy Pattern
The mathematical operations (`+`, `-`, `*`, `/`) are abstracted through the `MathOperationStrategy` interface. 
- Individual classes implement specific algorithms (e.g., `DivideStrategy`, `AddStrategy`).
- This satisfies the **Open-Closed Principle (OCP)**; adding a new math function (like exponentiation or modulus) only requires writing a new strategy class and registering it, without changing existing calculation or parsing routines.

### 2. Command Pattern
Every state-altering operation is encapsulated inside a `Command` object:
- `ArithmeticCommand` encapsulates mathematical operations and caches the previous value to support undoing.
- `ClearCommand` handles resetting the state to `0.000000` while preserving the undo capability.
- `CommandInvoker` manages an `undoStack` and a `redoStack` to guide history navigation. Pushing any new mathematical operation clears the redo history.

---

## Project Structure

```text
calculator/
├── .vscode/
│   └── settings.json           # VSCode settings configured for JavaSE-21
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── calculator/
│   │               ├── CalculatorApp.java      # Main Entry Point
│   │               ├── cli/
│   │               │   └── CalculatorCLI.java  # CLI Parser and IO Loop
│   │               ├── model/
│   │               │   └── Calculator.java     # Calculator State (Receiver)
│   │               ├── strategy/
│   │               │   ├── MathOperationStrategy.java # Strategy Interface
│   │               │   ├── AddStrategy.java
│   │               │   ├── SubtractStrategy.java
│   │               │   ├── MultiplyStrategy.java
│   │               │   └── DivideStrategy.java # Division-by-zero checks
│   │               └── command/
│   │                   ├── Command.java        # Command Interface
│   │                   ├── ArithmeticCommand.java
│   │                   ├── ClearCommand.java
│   │                   └── CommandInvoker.java # Undo/Redo Stacks Invoker
│   └── test/
│       └── java/
│           └── com/
│               └── calculator/
│                   └── CalculatorTest.java     # JUnit 5 Unit Tests
└── pom.xml                     # Maven configuration (Java 21 & JUnit 5)
```

---

## Prerequisites

- **Java Development Kit (JDK) 21**
- **Apache Maven** (for command-line builds and testing)

---

## How to Run & Test

### Running the Application

You can execute the calculator via your IDE (like VSCode or IntelliJ) by running the `main` method in `CalculatorApp.java`.

Alternatively, compile and execute it using Maven:

```bash
# Compile the project
mvn compile

# Execute the application
mvn exec:java -Dexec.mainClass="com.calculator.CalculatorApp"
```

### Running Tests

Run the full JUnit 5 unit test suite using:

```bash
mvn test
```

---

## Usage Examples

Once running, the interactive prompt will display the current calculation state and accept inputs:

```text
Current Result: 0.000000
Please enter a command (e.g., + 5, - 3, * 2, / 4, undo, redo, clear, exit):
+ 5.5

Current Result: 5.500000
Please enter a command (e.g., + 5, - 3, * 2, / 4, undo, redo, clear, exit):
* 2

Current Result: 11.000000
Please enter a command (e.g., + 5, - 3, * 2, / 4, undo, redo, clear, exit):
/ 4

Current Result: 2.750000
Please enter a command (e.g., + 5, - 3, * 2, / 4, undo, redo, clear, exit):
undo

Current Result: 11.000000
Please enter a command (e.g., + 5, - 3, * 2, / 4, undo, redo, clear, exit):
clear

Current Result: 0.000000
Please enter a command (e.g., + 5, - 3, * 2, / 4, undo, redo, clear, exit):
undo

Current Result: 11.000000
Please enter a command (e.g., + 5, - 3, * 2, / 4, undo, redo, clear, exit):
exit
```