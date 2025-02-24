package se.ifmo.client.builders;

import se.ifmo.client.console.Console;
import se.ifmo.system.collection.model.Vehicle;

import java.util.function.Consumer;
import java.util.function.Function;

public class VehicleDirector {
    public static Vehicle constructAndGetVehicle(Console console) throws InterruptedException {
        VehicleBuilder vehicleBuilder = new ConcreteVehicleBuilder();
        while (!input("name", vehicleBuilder::setName, Function.identity(), console)) ;

        CoordinatesBuilder coordinatesBuilder = new ConcreteCoordinatesBuilder();
        while (!input("x coordinate", coordinatesBuilder::setX, Long::valueOf, console)) ;
        while (!input("y coordinate", coordinatesBuilder::setY, Double::valueOf, console)) ;
        vehicleBuilder.setCoordinates(coordinatesBuilder.getResult());

        while (!input("engine power", vehicleBuilder::setEnginePower, Integer::valueOf, console)) ;
        while (!input("capacity", vehicleBuilder::setCapacity, Double::valueOf, console)) ;
        while (!input("distance travelled", vehicleBuilder::setDistanceTravelled, Float::valueOf, console)) ;
        return vehicleBuilder.getResult();
    }

    private static <K> boolean input(
            final String fieldName,
            final Consumer<K> setter,
            final Function<String, K> parser,
            final Console console
    ) throws InterruptedException {
        try {
            String line = console.read("Input next elements: " + fieldName);

            if (line == null) throw new InterruptedException("Input interrupted");
            else if (line.isBlank()) setter.accept(null);
            else setter.accept(parser.apply(line));

            return true;
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            console.writeln(e.getMessage());
            return false;
        }
    }
}

