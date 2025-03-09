package se.ifmo.client.builders;

import se.ifmo.system.collection.enums.FuelType;
import se.ifmo.system.collection.model.Coordinates;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;

/**
 * Concrete builder for a vehicle.
 */
public class ConcreteVehicleBuilder implements VehicleBuilder {
    private Vehicle vehicle;

    /**
     * Creates an empty {@link Vehicle} element.
     */
    public ConcreteVehicleBuilder() throws InvalidDataException {
        vehicle = new Vehicle();
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
    public void build() throws InvalidDataException {
        vehicle = new Vehicle();
    }

    @Override
    public Vehicle getResult() {
        return vehicle;
    }
}