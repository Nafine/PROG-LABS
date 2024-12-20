package ru.itmo.interfaces;

import ru.itmo.world.Place;

public interface CanEnjoy {
    public void haveFun(Place place, Place... where);
    public void eat(Place place, Place... where);
}
