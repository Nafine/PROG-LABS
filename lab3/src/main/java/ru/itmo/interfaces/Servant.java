package ru.itmo.interfaces;

import ru.itmo.exceptions.NoServantsHomeException;
import ru.itmo.persons.Servants;

public interface Servant {
    void lookAfterHome() throws NoServantsHomeException;
    void cleanHome() throws NoServantsHomeException;
    void serveMaster() throws NoServantsHomeException;
}
