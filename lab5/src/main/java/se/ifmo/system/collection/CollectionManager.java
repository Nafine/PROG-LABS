package se.ifmo.system.collection;

import lombok.Getter;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.collection.util.EnvManager;
import se.ifmo.system.file.csv.CSVHandler;

import java.io.IOException;
import java.util.LinkedHashSet;

/**
 * Singleton
 * <p>
 * Works with the collection. Stores it, loads from file and saves to file.
 * </p>
 */
@Getter
public class CollectionManager {
    private static CollectionManager instance;

    private final LinkedHashSet<Vehicle> collection = new LinkedHashSet<>();

    private CollectionManager() {
        load();
    }

    /**
     * Returns instance of {@link CollectionManager}
     * @return {@link CollectionManager}
     */
    public static CollectionManager getInstance() {
        return instance == null ? instance = new CollectionManager() : instance;
    }

    /**
     * Writes collection to the file.
     * <p>
     * Handles {@link IOException}.
     * </p>
     */
    public void save() {
        try (CSVHandler csvHandler = new CSVHandler(EnvManager.getDataFile(), false)) {
            csvHandler.write(collection);
        } catch (IOException e) {
            System.err.println("Failed to save collection");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Reads collection from file.
     * <p>
     * Handles {@link IOException}.
     * </p>
     */
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
