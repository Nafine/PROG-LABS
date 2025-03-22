package se.ifmo.server.file.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import se.ifmo.shared.model.Vehicle;
import se.ifmo.server.collection.util.Validatable;
import se.ifmo.shared.exceptions.InvalidDataException;

import java.util.LinkedHashSet;

/**
 * Wrapper for proper serialization of XML data.
 */
@Data
@JacksonXmlRootElement(localName = "vehicles")
public class VehicleXmlWrapper implements Validatable {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("vehicle")
    private LinkedHashSet<Vehicle> vehicles;

    public VehicleXmlWrapper() {
    }

    /**
     * Constructs a new {@link VehicleXmlWrapper} class.
     *
     * @param vehicles
     * @throws InvalidDataException
     */
    public VehicleXmlWrapper(LinkedHashSet<Vehicle> vehicles) throws InvalidDataException {
        this.vehicles = vehicles;
        validate();
    }

    /**
     * Validates whole collection.
     * <p>
     * Calls {@link Vehicle#validate()} on each element of collection.
     * </p>
     *
     * @throws InvalidDataException if collection data was invalid
     */
    @Override
    public void validate() throws InvalidDataException {
        for (var product : vehicles) product.validate();
    }
}
