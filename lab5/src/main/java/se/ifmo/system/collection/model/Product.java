package se.ifmo.system.collection.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import se.ifmo.system.collection.enums.UnitOfMeasure;
import se.ifmo.system.exceptions.InvalidDataException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class Product extends CollectionElement {
    private static Map<String, Integer> existingPartNumbers = new HashMap<>();

    private @NonNull String name;
    private @NonNull Coordinates coordinates;
    private @NonNull
    //идет расчет на использование jackson, поэтому оставлю это ЗДЕСЬ и не буду писать DTO
    @JsonIgnore java.util.Date creationDate;
    private int price;
    private @NonNull String partNumber;
    private @NonNull Integer manufactureCost;
    private @NonNull UnitOfMeasure unitOfMeasure;
    private @NonNull Organization manufacturer;

    public Product() {
        creationDate = new java.util.Date();
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;

        if (existingPartNumbers.containsKey(partNumber)) {
            existingPartNumbers.put(partNumber, existingPartNumbers.get(partNumber) + 1);
        }
        else {
            existingPartNumbers.put(partNumber, 1);
        }
    }

    @Override
    public void validate() throws InvalidDataException{
        if (id <= 0) throw new InvalidDataException(this, "Invalid ID");
        if (name.isEmpty()) throw new InvalidDataException(this, "Product name cannot be empty");
        if (price <= 0) throw new InvalidDataException(this, "Product price cannot be less than or equal to 0");
        if (partNumber.length() < 16) throw new InvalidDataException(this, "Product partNumber length cannot be less than 16");
        if (existingPartNumbers.get(partNumber) > 1) throw new InvalidDataException(this, "Product partNumber must be unique");
        manufacturer.validate();
        coordinates.validate();
    }
}
