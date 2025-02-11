package se.ifmo.system.collection.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
import se.ifmo.system.collection.enums.FuelType;
import se.ifmo.system.exceptions.InvalidDataException;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
//Идет расчет на использование библиотеки jackson, поэтому чтобы не городить миллион DTO там, где это и не нужно, я оставлю это здесь
@JsonPropertyOrder({"name", "coordinates", "enginePower", "capacity", "distanceTraveled", "fuelType"})
public class Vehicle extends CollectionElement {
    @NonNull
    private String name;

    @NonNull
    @JsonUnwrapped
    private Coordinates coordinates;

    @NonNull
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private java.util.Date creationDate;

    private int enginePower;
    private double capacity;
    private Float distanceTraveled;

    @NonNull
    private FuelType fuelType;

    public Vehicle() {
        creationDate = new java.util.Date();
    }

    @Override
    public void validate() throws InvalidDataException {
        if (id <= 0) throw new InvalidDataException(this, "Invalid ID");
        if (name.isEmpty()) throw new InvalidDataException(this, "Product name cannot be empty");
        if (enginePower <= 0) throw new InvalidDataException(this, "Engine power cannot equal or less than 0");
        if (capacity <= 0) throw new InvalidDataException(this, "Capacity cannot be equal or less than 0");
    }
}
