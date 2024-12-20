package ru.itmo.interfaces;

import ru.itmo.world.Place;

public interface Character {
    public void named();
    public void live();
    public void relocate(Place to);
}