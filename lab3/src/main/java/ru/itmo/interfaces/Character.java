package ru.itmo.interfaces;

import ru.itmo.world.Place;

public interface Character {
    void named();

    void live();

    void relocate(Place to);
}