package se.ifmo.client.builders;

import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.file.handler.IOHandler;

import java.util.function.Consumer;
import java.util.function.Function;

public class VehicleDirector {
    public static Vehicle constructAndGetVehicle(IOHandler<String> io) throws InterruptedException {
        VehicleBuilder vehicleBuilder = new ConcreteVehicleBuilder();
        while (!input("name", vehicleBuilder::setName, Function.identity(), io)) ;

        CoordinatesBuilder coordinatesBuilder = new ConcreteCoordinatesBuilder();
        while (!input("x coordinate", coordinatesBuilder::setX, Long::valueOf, io)) ;
        while (!input("y coordinate", coordinatesBuilder::setY, Double::valueOf, io)) ;
        vehicleBuilder.setCoordinates(coordinatesBuilder.getResult());

        while (!input("engine power", vehicleBuilder::setEnginePower, Integer::valueOf, io)) ;
        while (!input("capacity", vehicleBuilder::setCapacity, Double::valueOf, io)) ;
        while (!input("distance travelled", vehicleBuilder::setDistanceTravelled, Float::valueOf, io)) ;
        return vehicleBuilder.getResult();
    }

    private static <K> boolean input(
            final String fieldName,
            final Consumer<K> setter,
            final Function<String, K> parser,
            final IOHandler<String> io
    ) throws InterruptedException {
        try {
            String line = io.read("Input next elements: " + fieldName);

            if (line == null) throw new InterruptedException("Input interrupted");
            else if (line.isBlank()) setter.accept(null);
            else setter.accept(parser.apply(line));

            return true;
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            io.write(e.getMessage());
            io.write(System.lineSeparator());
            return false;
        }
    }
}

