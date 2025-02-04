package se.ifmo.system.file.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import se.ifmo.system.collection.enums.FuelType;
import se.ifmo.system.collection.model.Coordinates;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.file.handler.IOHandler;

import java.io.*;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

public class CSVHandler implements IOHandler<LinkedHashSet<Vehicle>> {
    private final Path csvFilePath;
    private final BufferedInputStream fileInputStream;
    private final BufferedWriter bufferedWriter;

    public static void main(String[] args) {
        try (CSVHandler csvHandler = new CSVHandler(Path.of(System.getenv("LAB5_DATA_PATH")))) {
            Vehicle vehicle = new Vehicle();
            vehicle.setCapacity(1);
            vehicle.setCoordinates(new Coordinates());
            vehicle.getCoordinates().setX(1);
            vehicle.getCoordinates().setY(2.0);
            vehicle.setName("Aboba");
            vehicle.setFuelType(FuelType.ALCOHOL);
            vehicle.setDistanceTraveled(123.0f);
            vehicle.setEnginePower(5);

            csvHandler.write(new LinkedHashSet<>(List.of(vehicle)));
            System.out.println(csvHandler.read());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public CSVHandler(Path csvFilePath) throws IOException {
        this.csvFilePath = csvFilePath;
        fileInputStream = new BufferedInputStream(new FileInputStream(csvFilePath.toFile()));
        bufferedWriter = new BufferedWriter(new FileWriter(csvFilePath.toFile()));
    }

    @Override
    public LinkedHashSet<Vehicle> read() {
        try {
            CsvMapper mapper = new CsvMapper();
            MappingIterator<Vehicle> it = mapper
                    .readerFor(Vehicle.class)
                    .with(mapper.schemaFor(Vehicle.class))
                    .readValues(fileInputStream);
            return new LinkedHashSet<>(it.readAll());
        } catch (Exception e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            System.out.println(e.getMessage());
        }

        return new LinkedHashSet<>();
    }

    @Override
    public void write(LinkedHashSet<Vehicle> vehicles) {
        try {
            CsvMapper csvMapper = new CsvMapper();
            SequenceWriter seqW = csvMapper.writerWithSchemaFor(Vehicle.class).writeValues(bufferedWriter);
            for (var vehicle : vehicles) {
                seqW.write(vehicle);
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + csvFilePath.getFileName());
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void close() {

    }
}
