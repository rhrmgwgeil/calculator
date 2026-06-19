package com.calculator.command;

/**
 * Command interface in the Command Pattern.
 */
public interface Command {
    /**
     * Executes the command.
     */
    void execute();

    /**
     * Undoes the command.
     */
    void undo();
}
