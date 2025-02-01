package se.ifmo.system.file.xml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import se.ifmo.system.collection.model.Product;
import se.ifmo.system.file.handler.FileHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.TreeSet;

public class XMLHandler extends FileHandler<TreeSet<Product>> {

    public XMLHandler() {
        super(Path.of(System.getenv("LAB5_DATA_PATH")));
    }

    @Override
    public TreeSet<Product> read() {
        try (FileReader fReader = getReader()) {
            XmlMapper mapper = new XmlMapper();
            return mapper.readValue(fReader, new TypeReference<>() {
            });
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Failed reading file: " + handlingFilePath);
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void write(TreeSet<Product> products) {
        try (FileOutputStream fStream = getOutputStream()) {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.writeValue(fStream, new ProductXmlWrapper(products));
        } catch (IOException e) {
            System.err.println("Failed writing to file " + handlingFilePath);
            System.err.println(e.getMessage());
        }
    }
}
