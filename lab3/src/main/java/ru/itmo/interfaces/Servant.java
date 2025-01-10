package ru.itmo.interfaces;

import ru.itmo.exceptions.NoServantsHomeException;

public interface Servant {
    void lookAfterHome() throws NoServantsHomeException;

    void cleanHome() throws NoServantsHomeException;

    void serveMaster() throws NoServantsHomeException;
}
