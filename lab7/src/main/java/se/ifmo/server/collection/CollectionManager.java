package se.ifmo.server.collection;

import lombok.Getter;
import se.ifmo.server.db.VehicleService;
import se.ifmo.shared.model.Vehicle;

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
     *
     * @return {@link CollectionManager}
     */
    public static CollectionManager getInstance() {
        return instance == null ? instance = new CollectionManager() : instance;
    }

    /**
     * Reads whole collection from file.
     * <p>
     * Handles {@link IOException}.
     * </p>
     */
    public void load() {
        collection.clear();
        collection.addAll(VehicleService.getInstance().getAll());
    }
}
