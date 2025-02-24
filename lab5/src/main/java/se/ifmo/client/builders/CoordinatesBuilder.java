package se.ifmo.client.builders;

import se.ifmo.system.collection.model.Coordinates;

public interface CoordinatesBuilder {
    public CoordinatesBuilder setX(long x);

    public CoordinatesBuilder setY(Double y);

    public void build();

    public Coordinates getResult();
}
