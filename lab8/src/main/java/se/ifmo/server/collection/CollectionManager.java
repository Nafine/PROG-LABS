package se.ifmo.server.collection;

import lombok.Getter;
import se.ifmo.server.db.VehicleService;
import se.ifmo.shared.model.Vehicle;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;

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
    public static synchronized CollectionManager getInstance() {
        return instance == null ? instance = new CollectionManager() : instance;
    }

    public synchronized void clearByUser(long uid) {
        VehicleService.getInstance().deleteByUser(uid);
    }

    public synchronized boolean removeById(long id) {
        if (VehicleService.getInstance().deleteById(id)) {
            collection.removeIf(vehicle -> vehicle.getId() == id);
            return true;
        }
        return false;
    }

    /**
     * Inserts new {@link Vehicle} into database.
     *
     * <p>
     * If the insertion was not successful, the item will not be added to the collection
     * </p>
     *
     * @param vehicle to add
     */
    public synchronized boolean add(Vehicle vehicle) {
        long vehicleId = VehicleService.getInstance().add(vehicle);
        if (vehicleId != -1) {
            vehicle.setId(vehicleId);
            collection.add(vehicle);
            return true;
        }
        return false;
    }

    public synchronized void addAll(List<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles)
            add(vehicle);
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
