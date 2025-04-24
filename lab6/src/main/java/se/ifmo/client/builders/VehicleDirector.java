package se.ifmo.client.builders;

import se.ifmo.shared.enums.FuelType;
import se.ifmo.shared.model.Vehicle;
import se.ifmo.shared.exceptions.InvalidDataException;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Director of builder pattern. Constructs a vehicle with given blueprint (arguments from commands)
 *
 * @see ConcreteVehicleBuilder
 * @see ConcreteCoordinatesBuilder
 */
public class VehicleDirector {
    private VehicleDirector() {}

    public static List<Vehicle> constructAndGetRandomVehicles(int amountToBuild) throws InvalidDataException {
        VehicleBuilder vehicleBuilder = new ConcreteVehicleBuilder();
        List<Vehicle> carFleet = new LinkedList<>();

        Random random = new Random();
        for (int i = 0; i < amountToBuild; i++) {
            vehicleBuilder.setName(String.valueOf(random.nextLong()));

            CoordinatesBuilder coordinatesBuilder = new ConcreteCoordinatesBuilder();
            coordinatesBuilder.setX(random.nextLong());
            coordinatesBuilder.setY(random.nextDouble() * random.nextInt());
            vehicleBuilder.setCoordinates(coordinatesBuilder.getResult());

            vehicleBuilder.setEnginePower(random.nextInt(1, Integer.MAX_VALUE));
            vehicleBuilder.setCapacity(random.nextInt(1, Integer.MAX_VALUE));
            vehicleBuilder.setDistanceTravelled(random.nextFloat() * random.nextInt(0, Integer.MAX_VALUE));
            vehicleBuilder.setFuelType(FuelType.values()[random.nextInt(0, Integer.MAX_VALUE) % FuelType.values().length]);
            vehicleBuilder.getResult();
            carFleet.add(vehicleBuilder.getResult());
            vehicleBuilder.build();
        }

        return carFleet;
    }

    public static Vehicle constructAndGetVehicle(List<String> blueprint) throws InvalidDataException {
        VehicleBuilder vehicleBuilder = new ConcreteVehicleBuilder();
        vehicleBuilder.setName(blueprint.get(0));

        CoordinatesBuilder coordinatesBuilder = new ConcreteCoordinatesBuilder();
        try {
            coordinatesBuilder.setX(Long.parseLong(blueprint.get(1)));
            coordinatesBuilder.setY(Double.valueOf(blueprint.get(2)));
            vehicleBuilder.setCoordinates(coordinatesBuilder.getResult());

            vehicleBuilder.setEnginePower(Integer.parseInt(blueprint.get(3)));
            vehicleBuilder.setCapacity(Integer.parseInt(blueprint.get(4)));
            vehicleBuilder.setDistanceTravelled(Float.valueOf(blueprint.get(5)));
            vehicleBuilder.setFuelType(FuelType.valueOf(blueprint.get(6)));
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException(vehicleBuilder, e.getMessage());
        }

        vehicleBuilder.getResult().validate();

        return vehicleBuilder.getResult();
    }
}
