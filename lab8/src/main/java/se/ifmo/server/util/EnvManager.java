package se.ifmo.server.util;

import lombok.Getter;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * Class which user for handling environment variables of the program.
 * <p>
 * Defines getters for both file paths: data and index files.
 * </p>
 */
public final class EnvManager {
    @Getter
    static int port;
    @Getter
    private static String databaseURL;
    @Getter
    private static String databaseUser;
    @Getter
    private static String databasePassword;

    static {
        try {
            port = Integer.parseInt(System.getenv("SERVER_PORT"));
            databaseURL = System.getenv("DATABASE_URL");
            databaseUser = System.getenv("DATABASE_USER");
            databasePassword = System.getenv("DATABASE_PASSWORD");
        } catch (InvalidPathException e) {
            System.out.println("Check setup files, given config file paths are not valid.");
        } catch (NumberFormatException e) {
            System.out.println("Damaged SERVER_PORT environment, variable is not a number.");
        } catch (NullPointerException e) {
            System.out.println("Check setup files, some of them are damaged or don't exist.");
            System.exit(1);
        }
    }
}
