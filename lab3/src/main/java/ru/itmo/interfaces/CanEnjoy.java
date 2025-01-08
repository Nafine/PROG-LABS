package ru.itmo.interfaces;

import ru.itmo.world.Place;

public interface CanEnjoy {
    void haveFun(Place place, Place... where);
    void eat(Place place, Place... where);
}
