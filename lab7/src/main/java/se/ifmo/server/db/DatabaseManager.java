package se.ifmo.server.db;

import se.ifmo.server.Server;
import se.ifmo.server.util.EnvManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class DatabaseManager {
    private static DatabaseManager instance;
    //base host - localhost, base port = 5432
    private Connection connection;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(EnvManager.getDatabaseURL(), EnvManager.getDatabaseUser(), EnvManager.getDatabasePassword());
        } catch (SQLException e) {
            Server.logger.log(Level.SEVERE, "Unable to connect to database", e);
            System.exit(1);
        }
    }

    public static DatabaseManager getInstance() throws SQLException {
        return (instance == null || instance.connection.isClosed()) ? instance = new DatabaseManager() : instance;
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) stmt.setObject(i + 1, params[i]);
        return stmt;
    }
}
