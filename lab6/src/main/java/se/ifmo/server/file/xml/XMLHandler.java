package se.ifmo.server.file.xml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import se.ifmo.server.file.CollectionParser;
import se.ifmo.server.file.FileHandler;
import se.ifmo.shared.exceptions.InvalidDataException;
import se.ifmo.shared.model.Vehicle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;

/**
 * Class which used for reading/writing XML files.
 */
public class XMLHandler implements CollectionParser<Vehicle> {
    private final Path filePath;

    private final FileHandler fileHandler;

    /**
     * Constructs a new {@link XMLHandler} class.
     *
     * @param filePath of handling file
     * @throws IOException if some {@link IOException} occurred
     */
    public XMLHandler(Path filePath) throws IOException {
        this.filePath = filePath;
        fileHandler = new FileHandler(filePath);
    }

    /**
     * Constructs a new {@link XMLHandler} class.
     *
     * @param filePath of handling file
     * @param append   parameter of {@link FileHandler} class
     * @throws IOException if some {@link IOException} occurred
     */
    public XMLHandler(Path filePath, boolean append) throws IOException {
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

        try {
            XmlMapper mapper = new XmlMapper();
            VehicleXmlWrapper vehicles = new VehicleXmlWrapper(mapper.readValue(fileHandler.getBufferedInputStream(), new TypeReference<>() {
            }));

            if (vehicles.getVehicles().isEmpty()) System.out.println("No vehicles found. Collection will be empty.");

            return vehicles.getVehicles();
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath.getFileName());
            System.err.println(e.getMessage());
        } catch (InvalidDataException e) {
            System.err.println("Error during serializing. Invalid xml file.");
            System.err.println(e.getMessage());
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
            VehicleXmlWrapper vehicleXmlWrapper = new VehicleXmlWrapper(vehicles);
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.writer().withDefaultPrettyPrinter().writeValue(fileHandler.getBufferedWriter(), vehicleXmlWrapper);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath.getFileName());
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
