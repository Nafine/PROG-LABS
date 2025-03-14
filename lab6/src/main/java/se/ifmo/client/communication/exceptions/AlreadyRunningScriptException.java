package se.ifmo.client.communication.exceptions;

/**
 * Checked exception which used for handling recursively running scripts.
 */
public class AlreadyRunningScriptException extends Exception {
    /**
     * Constructs a new {@link AlreadyRunningScriptException} exception.
     * @param scriptName name of recursively called script
     */
    public AlreadyRunningScriptException(String scriptName) {
        super(scriptName);
    }
}
