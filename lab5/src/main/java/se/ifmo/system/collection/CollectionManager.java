package se.ifmo.system.collection;

import lombok.Getter;
import se.ifmo.system.collection.model.Product;
import se.ifmo.system.file.xml.XMLHandler;

import java.util.Objects;
import java.util.TreeSet;

@Getter
public class CollectionManager {
    public static void main(String[] args) {
        CollectionManager manager = CollectionManager.getInstance();

        System.out.println(manager.getCollection());
        manager.save();
    }

    private static CollectionManager instance;

    private final TreeSet<Product> collection = new TreeSet<>();

    private CollectionManager() {
        load();
    }

    public static CollectionManager getInstance() {
        return Objects.isNull(instance) ? instance = new CollectionManager() : instance;
    }

    public void save() {
        XMLHandler xmlHandler = new XMLHandler();
        xmlHandler.write(collection);
    }

    public void load() {
        XMLHandler xmlHandler = new XMLHandler();
        collection.clear();
        collection.addAll(xmlHandler.read());
    }
}
