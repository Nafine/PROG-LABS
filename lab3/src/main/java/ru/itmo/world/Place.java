package ru.itmo.world;

import java.util.Objects;

public record Place(String name, String location) {
    public Place(String name) {
        this(name, "неизвестно где");
    }

    public Place(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        return Objects.equals(name, place.name) && Objects.equals(location, place.location);
    }
}
