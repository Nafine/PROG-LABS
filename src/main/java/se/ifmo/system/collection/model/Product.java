package se.ifmo.system.collection.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import se.ifmo.system.collection.enums.UnitOfMeasure;
import se.ifmo.system.collection.util.CollectionElement;

@Data
@EqualsAndHashCode(callSuper = false)
public class Product extends CollectionElement {
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

    static{
        Product product = new Product();
    }

    public Product() {
        creationDate = new java.util.Date();
    }

    @Override
    public boolean validate() {
        return !name.isEmpty() && price > 0 && partNumber.length() >= 16;
    }
}
