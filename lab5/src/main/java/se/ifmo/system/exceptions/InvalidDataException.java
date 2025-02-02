package se.ifmo.system.exceptions;

public class InvalidDataException extends Exception {
    private final Object obj;

    public InvalidDataException(Object obj, String message) {
        super(message);
        this.obj = obj;
    }

    @Override
    public String getMessage() {
        return obj.getClass() + ":" + super.getMessage();
    }
}
