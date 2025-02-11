package se.ifmo.system.collection;

import lombok.Getter;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.collection.util.EnvManager;
import se.ifmo.system.file.csv.CSVHandler;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Objects;

@Getter
public class CollectionManager {
    private static CollectionManager instance;

    private final LinkedHashSet<Vehicle> collection = new LinkedHashSet<>();

    private CollectionManager() {
        load();
    }

    public static CollectionManager getInstance() {
        return instance == null ? instance = new CollectionManager() : instance;
    }

    public void save() {
        try (CSVHandler csvHandler = new CSVHandler(EnvManager.getDataFile(), false)) {
            csvHandler.write(collection);
        } catch (IOException e) {
            System.err.println("Failed to save collection");
            System.err.println(e.getMessage());
        }
    }

    public void load() {
        try (CSVHandler csvHandler = new CSVHandler(EnvManager.getDataFile())) {
            collection.clear();
            collection.addAll(csvHandler.read());
        } catch (IOException e) {
            System.err.println("Failed to load collection");
            System.err.println(e.getMessage());
        }
    }
}
