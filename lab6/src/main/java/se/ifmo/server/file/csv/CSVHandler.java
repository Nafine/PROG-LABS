package se.ifmo.server.file.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import se.ifmo.server.Server;
import se.ifmo.server.file.CollectionParser;
import se.ifmo.server.file.FileHandler;
import se.ifmo.shared.exceptions.InvalidDataException;
import se.ifmo.shared.model.Vehicle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.logging.Level;

/**
 * Class which used for reading/writing CSV files.
 */
public class CSVHandler implements CollectionParser<Vehicle> {
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
            Server.logger.severe(String.format("File %s is not readable", filePath.getFileName()));
            return new LinkedHashSet<>();
        }

        CsvMapper mapper = new CsvMapper();
        try (MappingIterator<Vehicle> it = mapper
                .readerFor(Vehicle.class)
                .with(mapper.schemaFor(Vehicle.class))
                .readValues(fileHandler.getBufferedInputStream())) {

            LinkedHashSet<Vehicle> vehicles = new LinkedHashSet<>();
            int failedToRead = 0;
            while (it.hasNext()) {
                try {
                    Vehicle vehicle = it.next();
                    vehicle.validate();
                    vehicles.add(vehicle);
                } catch (InvalidDataException e) {
                    failedToRead++;
                    Server.logger.log(Level.INFO, String.format("Invalid data at line %s", it.getCurrentLocation()), e);
                } catch (Exception e) {
                    Server.logger.log(Level.INFO, "Error processing CSV line: ", e);
                }
            }
            if (failedToRead > 0)
                Server.logger.info(String.format("%d lines with invalid data skipped", failedToRead));
            Server.logger.fine(String.format("Read %d vehicles", vehicles.size()));
            return vehicles;
        } catch (IOException e) {
            Server.logger.log(Level.WARNING, "Something went wrong: ", e);
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
            Server.logger.severe(String.format("File %s is not writable", filePath.getFileName()));
            return;
        }

        CsvMapper mapper = new CsvMapper();
        try {
            SequenceWriter seqW = mapper.writerWithSchemaFor(Vehicle.class).writeValues(fileHandler.getBufferedWriter());
            for (var vehicle : vehicles) seqW.write(vehicle);
        } catch (IOException e) {
            Server.logger.log(Level.SEVERE, String.format("Error writing to CSV file %s", filePath.getFileName()), e);
        }
        Server.logger.fine("Successfully written to CSV file");
    }


    @Override
    public void close() throws IOException {
        fileHandler.close();
    }
}