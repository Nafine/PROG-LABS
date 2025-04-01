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
    private static Path dataFile;
    @Getter
    private static Path indexFile;

    static {
        try {
            port = Integer.parseInt(System.getenv("SERVER_PORT"));
            dataFile = Path.of(System.getenv("LAB6_DATA_PATH"));
            indexFile = Path.of(System.getenv("INDEX"));
        } catch (InvalidPathException e) {
            System.out.println("Check setup files, given config file paths are not valid.");
        } catch (NumberFormatException e) {
            System.out.println("Damaged SERVER_PORT file.");
        } catch (NullPointerException e) {
            System.out.println("Check setup files, some of them are damaged or don't exist.");
            System.exit(1);
        }
    }
}
