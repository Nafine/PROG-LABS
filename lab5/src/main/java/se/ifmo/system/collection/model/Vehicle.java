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
public class Vehicle extends CollectionElement implements Comparable<Vehicle> {
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

    public Vehicle(String name, long x, Double y, int enginePower, double capacity, Float distanceTraveled, FuelType fuelType) throws InvalidDataException {
        this();
        this.name = name;
        this.coordinates = new Coordinates(x,y);
        this.enginePower = enginePower;
        this.capacity = capacity;
        this.distanceTraveled = distanceTraveled;
        this.fuelType = fuelType;
        validate();
    }

    @Override
    public void validate() throws InvalidDataException {
        if (id <= 0) throw new InvalidDataException(this, "Invalid ID");
        if (name.isEmpty()) throw new InvalidDataException(this, "Product name cannot be empty");
        if (enginePower <= 0) throw new InvalidDataException(this, "Engine power cannot equal or less than 0");
        if (capacity <= 0) throw new InvalidDataException(this, "Capacity cannot be equal or less than 0");
    }

    @Override
    public int compareTo(Vehicle o) {
        if (this.getClass() != o.getClass()) throw new ClassCastException();
        if (this.equals(o)) return 0;

        return distanceTraveled < o.distanceTraveled ? -1 : 1;
    }
}
