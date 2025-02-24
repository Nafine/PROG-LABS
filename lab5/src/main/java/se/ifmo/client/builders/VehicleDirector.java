package se.ifmo.client.builders;

import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;

import java.util.List;

/**
 * Director of builder pattern. Constructs a vehicle with given blueprint (arguments from commands)
 *
 * @see ConcreteVehicleBuilder
 * @see ConcreteCoordinatesBuilder
 */
public class VehicleDirector {
    public static Vehicle constructAndGetVehicle(List<String> blueprint) throws NumberFormatException, InvalidDataException {
        VehicleBuilder vehicleBuilder = new ConcreteVehicleBuilder();
        vehicleBuilder.setName(blueprint.get(0));

        CoordinatesBuilder coordinatesBuilder = new ConcreteCoordinatesBuilder();
        coordinatesBuilder.setX(Long.parseLong(blueprint.get(1)));
        coordinatesBuilder.setY(Double.valueOf(blueprint.get(2)));
        vehicleBuilder.setCoordinates(coordinatesBuilder.getResult());

        vehicleBuilder.setEnginePower(Integer.parseInt(blueprint.get(3)));
        vehicleBuilder.setCapacity(Integer.parseInt(blueprint.get(4)));
        vehicleBuilder.setDistanceTravelled(Float.valueOf(blueprint.get(5)));

        vehicleBuilder.getResult().validate();

        return vehicleBuilder.getResult();
    }
}
