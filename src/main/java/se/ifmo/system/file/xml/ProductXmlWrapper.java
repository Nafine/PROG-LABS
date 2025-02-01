package se.ifmo.system.file.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import se.ifmo.system.collection.model.Product;

import java.util.TreeSet;

@Data
@JacksonXmlRootElement(localName = "products")
public class ProductXmlWrapper {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("product")
    private TreeSet<Product> products;

    public ProductXmlWrapper(TreeSet<Product> products) {
        this.products = products;
    }
}
