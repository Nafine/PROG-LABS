package se.ifmo.client.builders;

import se.ifmo.shared.enums.FuelType;
import se.ifmo.shared.model.Coordinates;
import se.ifmo.shared.model.Vehicle;
import se.ifmo.shared.exceptions.InvalidDataException;

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
    public void build() throws InvalidDataException;

    /**
     * Getter for built vehicle.
     * @return
     */
    public Vehicle getResult();
}