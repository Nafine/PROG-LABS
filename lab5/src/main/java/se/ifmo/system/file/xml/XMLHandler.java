package se.ifmo.system.file.xml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import se.ifmo.system.collection.model.Product;
import se.ifmo.system.exceptions.InvalidDataException;
import se.ifmo.system.file.handler.IOHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeSet;

public class XMLHandler implements IOHandler<TreeSet<Product>> {
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
    public TreeSet<Product> read() throws IOException {
        if (!Files.isReadable(xmlFilePath)) {
            System.err.println("File " + xmlFilePath.getFileName() + "is not readable");
            return new TreeSet<>();
        }

        try {
            XmlMapper mapper = new XmlMapper();
            ProductXmlWrapper products = new ProductXmlWrapper(mapper.readValue(fileReader, new TypeReference<>() {
            }));

            if (products.getProducts().isEmpty()) System.out.println("No products found. Collection will be empty.");

            return products.getProducts();
        } catch (InvalidDataException e) {
            System.err.println("Invalid XML file");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return new TreeSet<>();
    }

    @Override
    public void write(TreeSet<Product> products) throws IOException {
        if (!Files.isWritable(xmlFilePath)) {
            System.err.println("File " + xmlFilePath.getFileName() + "is not writable");
            throw new IOException();
        }

        try (FileOutputStream fStream = new FileOutputStream(xmlFilePath.toFile())) {
            ProductXmlWrapper productXmlWrapper = new ProductXmlWrapper(products);
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.writer().withDefaultPrettyPrinter().writeValue(fStream, productXmlWrapper);
        } catch (InvalidDataException e) {
            System.err.println("Invalid collection data. Can't deserialize");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void close() throws IOException {
        fileReader.close();
    }
}
