package ru.itmo.interfaces;

import ru.itmo.exceptions.NoServantsException;
import ru.itmo.persons.Servants;

public interface CanOwnServants {
    public void ownServants(Servants servants);
    public void punishServants() throws NoServantsException;
    public void fireServants() throws NoServantsException;
    public Servants.ServeQuality checkServeQuality() throws NoServantsException;
}
