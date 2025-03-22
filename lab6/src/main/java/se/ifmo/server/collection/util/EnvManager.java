package se.ifmo.server.collection.util;

import lombok.Getter;

import java.io.*;
import java.nio.file.Path;

/**
 * Class which user for handling environment variables of the program.
 * <p>
 * Defines getters for both file paths: data and index files.
 * </p>
 */
public final class EnvManager {
    @Getter
    private static Path dataFile;
    @Getter
    private static Path indexFile;

    static {
        try {
            dataFile = Path.of(System.getenv("LAB6_DATA_PATH"));
            indexFile = Path.of(System.getenv("INDEX"));
            Path errorFile = Path.of(System.getenv("ERROR_LOG"));
            System.setErr(new PrintStream(new FileOutputStream(errorFile.toFile())));
        } catch (IOException e) {
            System.out.println("ERROR_LOG file was not found, error logging will be performed in the console.");
        } catch (NullPointerException e) {
            System.err.println("CHECK environment variables, some of them was not defined.");
            System.exit(1);
        }
    }
}
