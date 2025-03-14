package se.ifmo.system.collection.util;

import lombok.Getter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
            dataFile = Path.of(System.getenv("LAB5_DATA_PATH"));
            indexFile = Path.of(System.getenv("INDEX"));
            Path errorFile = Path.of(System.getenv("ERROR_LOG"));
            System.setErr(new PrintStream(new FileOutputStream(errorFile.toFile())));
        } catch (IOException e) {
            System.out.println("ERROR_LOG file wan not found, error logging will be performed in the console.");
            System.exit(1);
        } catch (NullPointerException e) {
            System.out.println("CHECK environment variables, some of them was not defined.");
            System.exit(1);
        }
    }
}
