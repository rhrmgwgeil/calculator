package com.calculator.command;

import java.util.Stack;

/**
 * CommandInvoker manages the execution of commands, keeping track of history
 * using two stacks to implement undo and redo functionalities.
 */
public class CommandInvoker {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    /**
     * Executes a command, adds it to the undo history, and clears the redo history.
     *
     * @param command the command to execute
     */
    public void executeCommand(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        // Execute the command first. If an exception occurs, it won't be pushed.
        command.execute();

        undoStack.push(command);
        redoStack.clear(); // Any new operation clears the redo history
    }

    /**
     * Undoes the last executed command.
     */
    public void undo() {
        if (undoStack.isEmpty()) {
            throw new IllegalStateException("No operation to undo.");
        }
        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
    }

    /**
     * Redoes the last undone command.
     */
    public void redo() {
        if (redoStack.isEmpty()) {
            throw new IllegalStateException("No operation to redo.");
        }
        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
    }

    /**
     * Checks if undo can be performed.
     *
     * @return true if there are commands in the undo history, false otherwise
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Checks if redo can be performed.
     *
     * @return true if there are commands in the redo history, false otherwise
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
