package se.ifmo.shared.model;

import lombok.*;
import se.ifmo.shared.command.Add;

import java.io.Serializable;

/**
 * Data object.
 * <p>
 * Contains x and y coordinates of a vehicle.
 * </p>
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Coordinates implements Serializable {
    private long x;

    @NonNull
    private Double y;

    /**
     * Constructs a new {@link Add} command.
     */
    public Coordinates() {}

    /**
     * Constructs a new {@link Add} command.
     * @param x coordinate
     * @param y coordinate
     */
    public Coordinates(long x, Double y) {
        this.x = x;
        this.y = y;
    }
}
