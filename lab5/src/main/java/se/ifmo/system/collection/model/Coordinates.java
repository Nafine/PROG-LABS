package se.ifmo.system.collection.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonPropertyOrder({"x", "y"})
public class Coordinates {
    private long x;
    private @NonNull Double y;
}
