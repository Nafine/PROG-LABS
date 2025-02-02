package se.ifmo.system.collection.util;

import lombok.Getter;

import java.nio.file.Path;

public final class EnvManager {
    @Getter
    private static final Path dataFile;
    @Getter
    private static final Path indexFile;

    static {
        dataFile = Path.of(System.getenv("LAB5_DATA_PATH"));
        indexFile = Path.of(System.getenv("INDEX"));
    }
}
