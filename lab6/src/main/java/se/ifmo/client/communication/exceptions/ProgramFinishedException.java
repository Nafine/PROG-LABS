package se.ifmo.client.communication.exceptions;

/**
 * Checked exception which used for finishing the program.
 */
public class ProgramFinishedException extends RuntimeException {
    /**
     * Constructs an empty {@link ProgramFinishedException} exception.
     */
    public ProgramFinishedException() {
        super();
    }
}
