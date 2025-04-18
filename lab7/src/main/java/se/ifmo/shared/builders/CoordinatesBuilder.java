package se.ifmo.shared.builders;

import se.ifmo.shared.model.Coordinates;
import se.ifmo.shared.exceptions.InvalidDataException;

/**
 * Fluent coordinates builder interface.
 */
public interface CoordinatesBuilder {
    /**
     * Sets x param.
     * @param x coordinate
     * @return {@link CoordinatesBuilder}
     */
    public CoordinatesBuilder setX(long x);
    /**
     * Sets y param.
     * @param y coordinate
     * @return {@link CoordinatesBuilder}
     */
    public CoordinatesBuilder setY(Double y);
    /**
     * Clear state of coordinates (creates new {@link Coordinates} object).
     */
    public void build() throws InvalidDataException;
    /**
     * Getter for built coordinates.
     * @return {@link Coordinates}
     */
    public Coordinates getResult();
}