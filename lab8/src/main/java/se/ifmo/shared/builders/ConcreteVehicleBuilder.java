package se.ifmo.shared.builders;

import se.ifmo.shared.enums.FuelType;
import se.ifmo.shared.exceptions.InvalidDataException;
import se.ifmo.shared.model.Coordinates;
import se.ifmo.shared.model.Vehicle;

import java.util.Date;

/**
 * Concrete builder for a vehicle.
 */
public class ConcreteVehicleBuilder implements VehicleBuilder {
    private Vehicle vehicle;

    /**
     * Creates an empty {@link Vehicle} element.
     */
    public ConcreteVehicleBuilder() {
        vehicle = new Vehicle();
    }

    @Override
    public VehicleBuilder setId(long id) {
        vehicle.setId(id);
        return this;
    }

    @Override
    public VehicleBuilder setOwnerId(long id) {
        vehicle.setOwnerId(id);
        return this;
    }

    @Override
    public VehicleBuilder setName(String name) {
        vehicle.setName(name);
        return this;
    }

    @Override
    public VehicleBuilder setCoordinates(Coordinates coordinates) {
        vehicle.setCoordinates(coordinates);
        return this;
    }

    @Override
    public VehicleBuilder setCreationDate(Date date) {
        vehicle.setCreationDate(date);
        return this;
    }

    @Override
    public VehicleBuilder setEnginePower(int enginePower) {
        vehicle.setEnginePower(enginePower);
        return this;
    }

    @Override
    public VehicleBuilder setCapacity(double capacity) {
        vehicle.setCapacity(capacity);
        return this;
    }

    @Override
    public VehicleBuilder setDistanceTravelled(Float distanceTravelled) {
        vehicle.setDistanceTraveled(distanceTravelled);
        return this;
    }

    @Override
    public VehicleBuilder setFuelType(FuelType fuelType) {
        vehicle.setFuelType(fuelType);
        return this;
    }

    @Override
    public void buildNew() {
        vehicle = new Vehicle();
    }

    @Override
    public Vehicle getResult() {
        return vehicle;
    }
}