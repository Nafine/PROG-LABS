package se.ifmo.shared.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import se.ifmo.shared.enums.FuelType;
import se.ifmo.shared.exceptions.InvalidDataException;

import java.io.Serializable;

/**
 * Collection element.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Vehicle implements Comparable<Vehicle>, Serializable, Validatable {
    private long id;
    private long ownerId;

    @NonNull
    private String name;

    @NonNull
    private Coordinates coordinates;

    @NonNull
    private java.util.Date creationDate;

    private int enginePower;
    private double capacity;
    private Float distanceTravelled;

    @NonNull
    private FuelType fuelType;

    /**
     * Constructs a new {@link Vehicle} class.
     * <p>
     * Automatically sets creation date.
     * </p>
     */
    public Vehicle() {
        id = 1;
        creationDate = new java.util.Date();
    }

    /**
     * Constructs a new {@link Vehicle} class.
     *
     * @param name             of vehicle
     * @param x                coordinate x
     * @param y                coordinate y
     * @param enginePower      of vehicle
     * @param capacity         of vehicle
     * @param distanceTravelled of vehicle
     * @param fuelType         of vehicle
     * @throws InvalidDataException if some of the arguments were invalid
     */
    public Vehicle(String name, long x, Double y, int enginePower, double capacity, Float distanceTravelled, FuelType fuelType) throws InvalidDataException {
        this();
        this.name = name;
        this.coordinates = new Coordinates(x, y);
        this.enginePower = enginePower;
        this.capacity = capacity;
        this.distanceTravelled = distanceTravelled;
        this.fuelType = fuelType;
        validate();
    }

    /**
     * Used to validate all fields.
     *
     * @throws InvalidDataException if validation wasn't successful
     */
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

        return distanceTravelled < o.distanceTravelled ? -1 : 1;
    }
}