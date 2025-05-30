package se.ifmo.shared.builders;

import se.ifmo.shared.model.Coordinates;
import se.ifmo.shared.exceptions.InvalidDataException;

/**
 * Concrete builder for a coordinates.
 */
public class ConcreteCoordinatesBuilder implements CoordinatesBuilder {
    Coordinates coordinates;

    /**
     * Creates an empty {@link Coordinates} element.
     */
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
    public void build() throws InvalidDataException {
        coordinates = new Coordinates();
    }

    @Override
    public Coordinates getResult() {
        return coordinates;
    }
}