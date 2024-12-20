package ru.itmo.exceptions;

import ru.itmo.persons.Person;

public class NoServantsException extends Exception {
    Person master;
    public NoServantsException(Person master) {
        this.master = master;
    }

    @Override
    public String getMessage() {
        return master.toString() + " не имеет слуг!";
    }
}
