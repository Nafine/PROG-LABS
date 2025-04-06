package se.ifmo.server.db;

import se.ifmo.shared.builders.VehicleDirector;
import se.ifmo.shared.model.Vehicle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VehicleService {
    private static final String FIND_ALL_VEHICLES_SQL = "SELECT * FROM vehicle";
    private static final String ADD_VEHICLE = "INSERT INTO vehicle VALUES" +
            "(default, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING vehicle_id";
    private static final String DELETE_VEHICLE_BY_ID = "DELETE FROM vehicle WHERE vehicle_id = ?";


    public boolean deleteById(long id) {
        try (PreparedStatement stmt = DatabaseManager.getInstance().prepareStatement(DELETE_VEHICLE_BY_ID)) {
            stmt.setLong(0, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addVehicle(Vehicle vehicle) {
        try (PreparedStatement stmt = DatabaseManager.getInstance().prepareStatement(ADD_VEHICLE)) {

        }
    }

    public List<Vehicle> getAll() {
        try (PreparedStatement stmt = DatabaseManager.getInstance().prepareStatement(FIND_ALL_VEHICLES_SQL)) {
            ResultSet set = stmt.executeQuery();
            List<Vehicle> vehicles = new ArrayList<Vehicle>();
            while (set.next()) {
                vehicles.add(VehicleDirector.constructAndGetVehicle(set));
            }

            return vehicles;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    private void setParams(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) stmt.setObject(i + 1, params[i]);
    }
}
