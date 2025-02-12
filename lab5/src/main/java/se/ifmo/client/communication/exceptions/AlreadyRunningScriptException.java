package se.ifmo.client.communication.exceptions;

public class AlreadyRunningScriptException extends Exception {
    public AlreadyRunningScriptException(String scriptName) {
        super(scriptName);
    }
}
