package se.ifmo.system.collection.util;

import se.ifmo.client.console.Console;
import se.ifmo.system.collection.enums.FuelType;
import se.ifmo.system.collection.model.Coordinates;
import se.ifmo.system.collection.model.Vehicle;

import java.util.Arrays;
import java.util.function.Function;

public class VehicleReader {

    public static Vehicle readElement(Console console) throws InterruptedException {
        Vehicle vehicle = new Vehicle();

        vehicle.setCapacity(readField(console, "capacity", Double::valueOf));
        vehicle.setDistanceTraveled(readField(console, "distanceTraveled", Float::valueOf));
        vehicle.setName(readField(console, "name", String::valueOf));
        vehicle.setFuelType(readField(console, "fuelType: " + Arrays.toString(FuelType.values()), FuelType::valueOf));
        vehicle.setEnginePower(readField(console, "enginePower", Integer::valueOf));

        Coordinates coordinates = new Coordinates();
        coordinates.setX(readField(console, "coordinate x", Long::valueOf));
        coordinates.setY(readField(console, "coordinate y", Double::valueOf));

        vehicle.setCoordinates(coordinates);
        return vehicle;
    }


    private static <T> T readField(Console console, String fieldName, Function<String, T> parser) throws InterruptedException {
        String input = console.read(fieldName + ":");

        if (input == null || input.isBlank()) throw new InterruptedException("empty input");

        return parser.apply(input);
    }
}