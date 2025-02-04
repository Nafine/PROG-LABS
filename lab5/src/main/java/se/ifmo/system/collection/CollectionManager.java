package se.ifmo.system.collection;

import lombok.Getter;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.collection.util.EnvManager;
import se.ifmo.system.file.xml.XMLHandler;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.TreeSet;

@Getter
public class CollectionManager {
    public static void main(String[] args) {
        System.out.println("CollectionManager");
    }

    private static CollectionManager instance;

    private final LinkedHashSet<Vehicle> collection = new LinkedHashSet<>();

    private CollectionManager() {
        load();
    }

    public static CollectionManager getInstance() {
        return Objects.isNull(instance) ? instance = new CollectionManager() : instance;
    }

    public void save() {
        try(XMLHandler xmlHandler = new XMLHandler(EnvManager.getDataFile())) {
            xmlHandler.write(collection);
        }
        catch(IOException e){
            System.err.println("Failed to save collection");
            System.err.println(e.getMessage());
        }
    }

    public void load() {
        try(XMLHandler xmlHandler = new XMLHandler(EnvManager.getDataFile())) {
            collection.clear();
            collection.addAll(xmlHandler.read());
        }
        catch(IOException e){
            System.err.println("Failed to load collection");
            System.err.println(e.getMessage());
        }
    }
}
