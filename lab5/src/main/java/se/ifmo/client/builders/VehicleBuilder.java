package se.ifmo.client.builders;

import se.ifmo.system.collection.enums.FuelType;
import se.ifmo.system.collection.model.Coordinates;
import se.ifmo.system.collection.model.Vehicle;

public interface VehicleBuilder {
    public VehicleBuilder setName(String name);

    public VehicleBuilder setCoordinates(Coordinates coordinates);

    public VehicleBuilder setEnginePower(int power);

    public VehicleBuilder setCapacity(double capacity);

    public VehicleBuilder setDistanceTravelled(Float distanceTravelled);

    public VehicleBuilder setFuelType(FuelType fuelType);

    public void build();

    public Vehicle getResult();
}
