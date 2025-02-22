package se.ifmo.system.collection.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import se.ifmo.client.command.Add;

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
@JsonPropertyOrder({"x", "y"})
public class Coordinates {
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
