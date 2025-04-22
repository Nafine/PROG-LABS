package se.ifmo.shared.builders;

import se.ifmo.shared.enums.FuelType;
import se.ifmo.shared.exceptions.InvalidDataException;
import se.ifmo.shared.model.Coordinates;
import se.ifmo.shared.model.Vehicle;

import java.util.Date;

/**
 * Fluent vehicle builder interface.
 */
public interface VehicleBuilder {
    /**
     * Sets vehicle id
     *
     * @param id of vehicle
     * @return {@link VehicleBuilder}
     */
    VehicleBuilder setId(long id);

    /**
     * Sets owner id
     *
     * @param id of owner
     * @return {@link VehicleBuilder}
     */
    VehicleBuilder setOwnerId(long id);

    /**
     * Sets vehicle name
     *
     * @param name of vehicle
     * @return {@link VehicleBuilder}
     */
    VehicleBuilder setName(String name);

    /**
     * Sets coordinates of a vehicle
     *
     * @param coordinates of vehicle
     * @return {@link VehicleBuilder}
     */
    VehicleBuilder setCoordinates(Coordinates coordinates);

    /**
     * Sets new creation date of a vehicle
     *
     * @param creationDate of vehicle
     * @return {@link VehicleBuilder}
     */
    VehicleBuilder setCreationDate(Date creationDate);

    /**
     * Sets engine power of a vehicle.
     *
     * @param enginePower of vehicle
     * @return {@link VehicleBuilder}
     */
    VehicleBuilder setEnginePower(int enginePower);

    /**
     * Sets capacity of a vehicle.
     *
     * @param capacity of vehicle
     * @return {@link VehicleBuilder}
     */
    VehicleBuilder setCapacity(double capacity);

    /**
     * Sets distance, which vehicle travelled.
     *
     * @param distanceTravelled of vehicle
     * @return {@link VehicleBuilder}
     */
    VehicleBuilder setDistanceTravelled(Float distanceTravelled);

    /**
     * Sets fuel type of vehicle.
     *
     * @param fuelType of vehicle
     * @return {@link VehicleBuilder}
     */
    VehicleBuilder setFuelType(FuelType fuelType);

    /**
     * Clear state of a vehicle (creates new {@link Vehicle} object).
     */
    void buildNew() throws InvalidDataException;

    /**
     * Getter for built vehicle.
     *
     * @return built vehicle
     */
    Vehicle getResult();
}