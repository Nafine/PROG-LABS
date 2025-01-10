package ru.itmo.interfaces;

import ru.itmo.exceptions.NoServantsException;
import ru.itmo.persons.Servants;

public interface CanOwnServants {
    void ownServants(Servants servants);

    void punishServants() throws NoServantsException;

    void fireServants() throws NoServantsException;

    Servants.ServeQuality checkServeQuality() throws NoServantsException;
}
