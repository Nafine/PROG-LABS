package se.ifmo.system.file.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;
import se.ifmo.system.file.FileHandler;
import se.ifmo.system.file.handler.IOHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;

/**
 * Class which used for reading/writing CSV file.
 */
public class CSVHandler implements IOHandler<LinkedHashSet<Vehicle>> {
    private final Path filePath;
    private final FileHandler fileHandler;

    /**
     * Constructs a new {@link CSVHandler} class.
     *
     * @param filePath of handling file
     * @throws IOException if some {@link IOException} occurred
     */
    public CSVHandler(Path filePath) throws IOException {
        this.filePath = filePath;
        fileHandler = new FileHandler(filePath);
    }

    /**
     * Constructs a new {@link CSVHandler} class.
     *
     * @param filePath of handling file
     * @param append   parameter of {@link FileHandler} class
     * @throws IOException if some {@link IOException} occurred
     */
    public CSVHandler(Path filePath, boolean append) throws IOException {
        this.filePath = filePath;
        fileHandler = new FileHandler(filePath, append);
    }

    /**
     * Reads collection elements from file by lines.
     * <p>
     * Adds element only if line which represents collection element was valid.
     * Handles all {@link IOException}s.
     * </p>
     *
     * @return {@link LinkedHashSet} with elements type of {@link Vehicle}
     */
    @Override
    public LinkedHashSet<Vehicle> read() {
        if (!Files.isReadable(filePath)) {
            System.err.println("File " + filePath.getFileName() + "is not readable");
            return new LinkedHashSet<>();
        }

        CsvMapper mapper = new CsvMapper();
        try (MappingIterator<Vehicle> it = mapper
                .readerFor(Vehicle.class)
                .with(mapper.schemaFor(Vehicle.class))
                .readValues(fileHandler.getBufferedInputStream())) {

            LinkedHashSet<Vehicle> vehicles = new LinkedHashSet<>();

            while (it.hasNext()) {
                try {
                    Vehicle vehicle = it.next();
                    vehicle.validate();
                    vehicles.add(vehicle);
                } catch (InvalidDataException e) {
                    System.out.println("Invalid vehicle data: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error processing CSV line: " + e.getMessage());
                }
            }
            return vehicles;
        } catch (IOException e) {
            System.err.println("Something went wrong: " + e.getMessage());
        }
        return new LinkedHashSet<>();
    }

    /**
     * Writes collection elements to the file.
     * <p>
     * Handles all {@link IOException}s.
     * </p>
     */
    @Override
    public void write(LinkedHashSet<Vehicle> vehicles) {
        if (!Files.isWritable(filePath)) {
            System.err.println("File " + filePath.getFileName() + "is not writable");
            return;
        }

        try {
            CsvMapper csvMapper = new CsvMapper();
            SequenceWriter seqW = csvMapper.writerWithSchemaFor(Vehicle.class).writeValues(fileHandler.getBufferedWriter());
            for (var vehicle : vehicles) {
                seqW.write(vehicle);
                vehicle.validate();
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + filePath.getFileName());
            System.err.println(e.getMessage());
        } catch (InvalidDataException e) {
            System.err.println("Error during deserializing. Invalid collection data.");
            System.err.println(e.getMessage());
        }
    }


    @Override
    public void close() throws IOException {
        fileHandler.close();
    }
}