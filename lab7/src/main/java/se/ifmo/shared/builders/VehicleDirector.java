package se.ifmo.shared.builders;

import se.ifmo.shared.enums.FuelType;
import se.ifmo.shared.exceptions.InvalidDataException;
import se.ifmo.shared.model.Vehicle;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    private VehicleDirector() {
    }

    public static Vehicle constructAndGetVehicle(ResultSet set) throws SQLException {
        VehicleBuilder vehicleBuilder = new ConcreteVehicleBuilder();

        vehicleBuilder.setId(set.getLong("vehicle_id"));
        vehicleBuilder.setName(set.getString("name"));
        vehicleBuilder.setCreationDate(set.getTimestamp("creation_date"));

        CoordinatesBuilder coordinatesBuilder = new ConcreteCoordinatesBuilder();
        coordinatesBuilder.setX(set.getLong("coord_x"));
        coordinatesBuilder.setY(set.getDouble("coord_y"));

        vehicleBuilder.setCoordinates(coordinatesBuilder.getResult());
        vehicleBuilder.setEnginePower(set.getInt("engine_power"));
        vehicleBuilder.setCapacity(set.getInt("capacity"));
        vehicleBuilder.setDistanceTravelled(set.getFloat("distance_travelled"));
        vehicleBuilder.setFuelType(FuelType.valueOf(set.getString("fuel_type")));
        vehicleBuilder.setOwnerId(set.getLong("uid"));

        return vehicleBuilder.getResult();
    }

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
            vehicleBuilder.buildNew();
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
