package se.ifmo.client.builders;

import se.ifmo.system.collection.model.Coordinates;

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
    public void build();
    /**
     * Getter for built coordinates.
     * @return {@link Coordinates}
     */
    public Coordinates getResult();
}