package se.ifmo.server.util;

import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
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
            Path errorFile = Path.of(System.getenv("ERROR_LOG"));
            System.setErr(new PrintStream(new FileOutputStream(errorFile.toFile())));
        } catch (InvalidPathException e) {
            System.out.println("Check setup files, given config file paths are not valid.");
        } catch (FileNotFoundException e) {
            System.out.println("Check setup files, wasn't able to find ERROR_LOG file, error logging will be performed into the console.");
        } catch (NumberFormatException e) {
            System.out.println("Damaged SERVER_PORT file.");
        } catch (NullPointerException e) {
            System.out.println("Check setup files, some of them are damaged or don't exist.");
            System.exit(1);
        }
    }
}
