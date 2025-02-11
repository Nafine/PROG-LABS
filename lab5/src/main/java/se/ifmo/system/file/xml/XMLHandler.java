package se.ifmo.system.file.xml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;
import se.ifmo.system.file.FileHandler;
import se.ifmo.system.file.handler.IOHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;

public class XMLHandler implements IOHandler<LinkedHashSet<Vehicle>> {
    private final Path filePath;

    private final FileHandler fileHandler;

    public XMLHandler(Path filePath) throws IOException {
        this.filePath = filePath;
        fileHandler = new FileHandler(filePath);
    }

    public XMLHandler(Path filePath, boolean append) throws IOException {
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
