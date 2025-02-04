package se.ifmo.system.file.xml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.exceptions.InvalidDataException;
import se.ifmo.system.file.handler.IOHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;

public class XMLHandler implements IOHandler<LinkedHashSet<Vehicle>> {
    private final Path xmlFilePath;
    private final FileReader fileReader;

    public XMLHandler(Path xmlFilePath) throws IOException {
        this.xmlFilePath = xmlFilePath;

        if (Files.notExists(xmlFilePath)) {
            System.err.println("File " + xmlFilePath.getFileName() + "is not found");
            throw new FileNotFoundException();
        }

        fileReader = new FileReader(xmlFilePath.toFile());
    }

    @Override
    public LinkedHashSet<Vehicle> read() {
        if (!Files.isReadable(xmlFilePath)) {
            System.err.println("File " + xmlFilePath.getFileName() + "is not readable");
            return new LinkedHashSet<>();
        }

        try {
            XmlMapper mapper = new XmlMapper();
            VehicleXmlWrapper vehicles = new VehicleXmlWrapper(mapper.readValue(fileReader, new TypeReference<>() {
            }));

            if (vehicles.getVehicles().isEmpty()) System.out.println("No vehicles found. Collection will be empty.");

            return vehicles.getVehicles();
        } catch (IOException e) {
            System.err.println("Error reading file " + xmlFilePath.getFileName());
            System.err.println(e.getMessage());
        } catch (InvalidDataException e) {
            System.err.println("Error during serializing. Invalid xml file.");
            System.err.println(e.getMessage());
        }

        return new LinkedHashSet<>();
    }

    @Override
    public void write(LinkedHashSet<Vehicle> vehicles) {
        if (!Files.isWritable(xmlFilePath)) {
            System.err.println("File " + xmlFilePath.getFileName() + "is not writable");
            return;
        }

        try (FileOutputStream fStream = new FileOutputStream(xmlFilePath.toFile())) {
            VehicleXmlWrapper vehicleXmlWrapper = new VehicleXmlWrapper(vehicles);
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.writer().withDefaultPrettyPrinter().writeValue(fStream, vehicleXmlWrapper);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + xmlFilePath.getFileName());
            System.err.println(e.getMessage());
        } catch (InvalidDataException e) {
            System.err.println("Error during deserializing. Invalid collection data.");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        fileReader.close();
    }
}
