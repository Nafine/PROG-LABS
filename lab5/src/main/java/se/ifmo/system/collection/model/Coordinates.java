package se.ifmo.system.collection.model;

import lombok.*;
import se.ifmo.system.collection.util.Validatable;
import se.ifmo.system.exceptions.InvalidDataException;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Coordinates implements Validatable {
    private @NonNull Double x;
    private int y;

    @Override
    public void validate() throws InvalidDataException {
        if (x > 367) throw new InvalidDataException(this, "Coordinates x cannot be higher than 367");
        if (y <= -140) throw new InvalidDataException(this, "Coordinates y cannot be lower than 140");
    }
}
