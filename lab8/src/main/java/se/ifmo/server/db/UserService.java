package se.ifmo.server.db;

import se.ifmo.server.Server;
import se.ifmo.shared.communication.Credentials;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class UserService {
    private static final String FIND_UID_BY_NAME = "SELECT uid FROM SERVER_USER WHERE LOGIN = ?";
    private static final String ADD_USER = "INSERT INTO server_user (uid, login, password) VALUES (default, ?, ?)";
    private static final String FIND_PASSWORD_BY_LOGIN = "SELECT password FROM SERVER_USER WHERE LOGIN = ?";
    private static UserService instance;

    private UserService() {
    }

    public static synchronized UserService getInstance() {
        return instance == null ? instance = new UserService() : instance;
    }

    public boolean addUser(Credentials credentials) {
        try (PreparedStatement stmt = DatabaseManager.getInstance().prepareStatement(ADD_USER, credentials.username(), credentials.password())) {
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Server.logger.log(Level.SEVERE, "Cannot add user to database", e);
            return false;
        }
    }

    public long getUserID(String login) {
        try (PreparedStatement stmt = DatabaseManager.getInstance().prepareStatement(FIND_UID_BY_NAME, login)) {
            ResultSet set = stmt.executeQuery();
            if (set.next()) return set.getLong("uid");
            return -1;
        } catch (SQLException e) {
            Server.logger.log(Level.INFO, "Can't get user ID for login " + login, e);
            return -1;
        }
    }

    public boolean comparePassword(Credentials credentials) {
        try (PreparedStatement stmt = DatabaseManager.getInstance().prepareStatement(FIND_PASSWORD_BY_LOGIN, credentials.username())) {
            ResultSet set = stmt.executeQuery();
            if (set.next()) return set.getString("password").equals(credentials.password());
            return false;
        } catch (SQLException e) {
            Server.logger.log(Level.WARNING, "Cannot compare password for login " + credentials.username(), e);
            return false;
        }
    }
}
