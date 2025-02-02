package se.ifmo.system.file.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import se.ifmo.system.collection.model.Product;
import se.ifmo.system.collection.util.Validatable;
import se.ifmo.system.exceptions.InvalidDataException;

import java.util.TreeSet;

@Data
@JacksonXmlRootElement(localName = "products")
public class ProductXmlWrapper implements Validatable {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("product")
    private TreeSet<Product> products;

    public ProductXmlWrapper() {}

    public ProductXmlWrapper(TreeSet<Product> products) throws InvalidDataException {
        this.products = products;
        validate();
    }

    @Override
    public void validate() throws InvalidDataException {
        for (var product : products) product.validate();
    }
}
