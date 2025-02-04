package se.ifmo.system.file.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.collection.util.Validatable;
import se.ifmo.system.exceptions.InvalidDataException;

import java.util.LinkedHashSet;
import java.util.TreeSet;

@Data
@JacksonXmlRootElement(localName = "vehicles")
public class VehicleXmlWrapper implements Validatable {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("vehicle")
    private LinkedHashSet<Vehicle> vehicles;

    public VehicleXmlWrapper() {}

    public VehicleXmlWrapper(LinkedHashSet<Vehicle> vehicles) throws InvalidDataException {
        this.vehicles = vehicles;
        validate();
    }

    @Override
    public void validate() throws InvalidDataException {
        for (var product : vehicles) product.validate();
    }
}
