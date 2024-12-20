package ru.itmo.interfaces;

import ru.itmo.exceptions.NoServantsHomeException;
import ru.itmo.persons.Servants;

public interface Servant {
    public void lookAfterHome() throws NoServantsHomeException;
    public void cleanHome() throws NoServantsHomeException;
    public void serveMaster() throws NoServantsHomeException;
}
