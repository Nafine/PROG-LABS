package se.ifmo.client.builders;

import se.ifmo.system.collection.model.Coordinates;

public class ConcreteCoordinatesBuilder implements CoordinatesBuilder {
    Coordinates coordinates;

    public ConcreteCoordinatesBuilder() {
        coordinates = new Coordinates();
    }

    @Override
    public CoordinatesBuilder setX(long x) {
        coordinates.setX(x);
        return this;
    }

    @Override
    public CoordinatesBuilder setY(Double y) {
        coordinates.setY(y);
        return this;
    }

    @Override
    public void build() {
        coordinates = new Coordinates();
    }

    @Override
    public Coordinates getResult() {
        return coordinates;
    }
}
