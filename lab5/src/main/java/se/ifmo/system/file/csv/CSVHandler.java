package se.ifmo.system.file.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;
import se.ifmo.system.file.FileHandler;
import se.ifmo.system.file.handler.IOHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;

public class CSVHandler implements IOHandler<LinkedHashSet<Vehicle>> {
    private final Path filePath;
    private final FileHandler fileHandler;

    public CSVHandler(Path filePath) throws IOException {
        this.filePath = filePath;
        fileHandler = new FileHandler(filePath);
    }

    public CSVHandler(Path filePath, boolean append) throws IOException {
        this.filePath = filePath;
        fileHandler = new FileHandler(filePath, append);
    }

    @Override
    public LinkedHashSet<Vehicle> read() {
        if (!Files.isReadable(filePath)) {
            System.err.println("File " + filePath.getFileName() + "is not readable");
            return new LinkedHashSet<>();
        }

        try {
            CsvMapper mapper = new CsvMapper();
            MappingIterator<Vehicle> it = mapper
                    .readerFor(Vehicle.class)
                    .with(mapper.schemaFor(Vehicle.class))
                    .readValues(fileHandler.getBufferedInputStream());
            LinkedHashSet<Vehicle> vehicles= new LinkedHashSet<>(it.readAll());
            for (Vehicle vehicle : vehicles) {
                vehicle.validate();
            }
            return vehicles;
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        catch (InvalidDataException e) {
            System.err.println("Error during deserializing. Invalid collection data.");
            System.err.println(e.getMessage());
        }

        return new LinkedHashSet<>();
    }

    @Override
    public void write(LinkedHashSet<Vehicle> vehicles) {
        if (!Files.isWritable(filePath)) {
            System.err.println("File " + filePath.getFileName() + "is not writable");
            return;
        }

        try{
            CsvMapper csvMapper = new CsvMapper();
            SequenceWriter seqW = csvMapper.writerWithSchemaFor(Vehicle.class).writeValues(fileHandler.getBufferedWriter());
            for (var vehicle : vehicles) {
                seqW.write(vehicle);
                vehicle.validate();
            }
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + filePath.getFileName());
            System.out.println(e.getMessage());
        }
        catch (InvalidDataException e) {
            System.err.println("Error during deserializing. Invalid collection data.");
            System.err.println(e.getMessage());
        }
    }


    @Override
    public void close() throws IOException {
        fileHandler.close();
    }
}
