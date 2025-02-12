package se.ifmo.client.command.util;

import se.ifmo.system.collection.enums.FuelType;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;

import java.util.List;

public class VehicleReader {

    public static Vehicle readElement(List<String> args) throws IndexOutOfBoundsException, InvalidDataException {
        String name = args.get(0);
        long x = Long.parseLong(args.get(1));
        Double y = Double.parseDouble(args.get(2));
        int enginePower = Integer.parseInt(args.get(3));
        double capacity = Double.parseDouble(args.get(4));
        Float distanceTraveled = Float.parseFloat(args.get(5));
        FuelType fuelType = FuelType.valueOf(args.get(6));

        return new Vehicle(name, x, y, enginePower, capacity, distanceTraveled, fuelType);
    }
}