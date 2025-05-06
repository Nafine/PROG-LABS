package se.ifmo.server.db;

import se.ifmo.server.Server;
import se.ifmo.shared.builders.VehicleDirector;
import se.ifmo.shared.model.Vehicle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class VehicleService {
    private static final String FIND_ALL_VEHICLES = "SELECT * FROM vehicle";
    private static final String ADD_VEHICLE = "INSERT INTO vehicle (vehicle_id, name, creation_date, engine_power, "
            + "capacity, distance_travelled, fuel_type, coord_x, coord_y, uid)"
            + "VALUES (default, ?, default, ?, ?, ?, ?, ?, ?, ?) RETURNING vehicle_id";
    private static final String DELETE_VEHICLE_BY_ID = "DELETE FROM vehicle WHERE vehicle_id = ?";
    private static final String DELETE_VEHICLE_BY_USER = "DELETE FROM vehicle WHERE uid = ?";
    private static VehicleService instance;

    private VehicleService() {
    }

    public static VehicleService getInstance() {
        return instance == null ? instance = new VehicleService() : instance;
    }

    public void deleteByUser(long uid) {
        try (PreparedStatement stmt = DatabaseManager.getInstance().prepareStatement(DELETE_VEHICLE_BY_USER)) {
            stmt.setLong(1, uid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Server.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public boolean deleteById(long id) {
        try (PreparedStatement stmt = DatabaseManager.getInstance().prepareStatement(DELETE_VEHICLE_BY_ID)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Server.logger.log(Level.INFO, "Failed delete vehicle from database", e);
            return false;
        }
    }

    public List<Vehicle> getAll() {
        try (PreparedStatement stmt = DatabaseManager.getInstance().prepareStatement(FIND_ALL_VEHICLES)) {
            ResultSet set = stmt.executeQuery();
            List<Vehicle> vehicles = new ArrayList<>();
            while (set.next()) vehicles.add(VehicleDirector.constructAndGetVehicle(set));
            return vehicles;
        } catch (SQLException e) {
            Server.logger.log(Level.SEVERE, "Failed to get collection from database", e);
            return Collections.emptyList();
        }
    }

    public long add(Vehicle vehicle) {
        try (PreparedStatement stmt = DatabaseManager.getInstance().prepareStatement(ADD_VEHICLE)) {
            setParams(stmt, vehicle.getName(), vehicle.getEnginePower(), vehicle.getCapacity(), vehicle.getDistanceTravelled(),
                    vehicle.getFuelType().toString(), vehicle.getCoordinates().getX(), vehicle.getCoordinates().getY(), vehicle.getOwnerId());
            ResultSet set = stmt.executeQuery();
            if (set.next()) return set.getLong(1);
            return -1;
        } catch (SQLException e) {
            Server.logger.log(Level.INFO, "Failed to add vehicle: " + vehicle, e);
            return -1;
        }
    }

    private void setParams(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) stmt.setObject(i + 1, params[i]);
    }
}
