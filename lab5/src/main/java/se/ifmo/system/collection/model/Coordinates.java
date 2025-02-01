package se.ifmo.system.collection.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import se.ifmo.system.collection.util.Validatable;

@Getter
@Setter
@EqualsAndHashCode
public class Coordinates implements Validatable {


    private @NonNull Double x;
    private int y;

    @Override
    public boolean validate() {
        return x <= 367 && y > -140;
    }
}
