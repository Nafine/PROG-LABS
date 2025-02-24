package se.ifmo.client.builders;

import se.ifmo.system.collection.enums.FuelType;
import se.ifmo.system.collection.model.Coordinates;
import se.ifmo.system.collection.model.Vehicle;

/**
 * Fluent vehicle builder interface.
 */
public interface VehicleBuilder {
    /**
     * Sets vehicle name
     *
     * @param name of vehicle
     * @return {@link VehicleBuilder}
     */
    public VehicleBuilder setName(String name);

    /**
     * Sets coordinates of a vehicle
     *
     * @param coordinates of vehicle
     * @return {@link VehicleBuilder}
     */
    public VehicleBuilder setCoordinates(Coordinates coordinates);

    /**
     * Sets engine power of a vehicle.
     *
     * @param enginePower of vehicle
     * @return {@link VehicleBuilder}
     */
    public VehicleBuilder setEnginePower(int enginePower);

    /**
     * Sets capacity of a vehicle.
     *
     * @param capacity of vehicle
     * @return {@link VehicleBuilder}
     */
    public VehicleBuilder setCapacity(double capacity);

    /**
     * Sets distance, which vehicle travelled.
     *
     * @param distanceTravelled of vehicle
     * @return {@link VehicleBuilder}
     */
    public VehicleBuilder setDistanceTravelled(Float distanceTravelled);

    /**
     * Sets fuel type of vehicle.
     *
     * @param fuelType of vehicle
     * @return {@link VehicleBuilder}
     */
    public VehicleBuilder setFuelType(FuelType fuelType);

    /**
     * Clear state of a vehicle (creates new {@link Vehicle} object).
     */
    public void build();

    /**
     * Getter for built vehicle.
     * @return
     */
    public Vehicle getResult();
}