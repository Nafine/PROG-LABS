package ru.itmo.exceptions;

import ru.itmo.persons.Servants;

public class NoServantsHomeException extends Exception {
    Servants servants;
    public NoServantsHomeException(Servants servants) {
        this.servants = servants;
    }

    @Override
    public String getMessage() {
        return "Слуги не прикреплены к дому:\n" + servants.toString();
    }
}
