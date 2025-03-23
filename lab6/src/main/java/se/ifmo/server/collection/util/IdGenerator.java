package se.ifmo.server.collection.util;

import se.ifmo.server.util.EnvManager;
import se.ifmo.server.file.FileHandler;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Singleton.
 * <p>
 * Class which user for generating unique id even after restart.
 * </p>
 */
public class IdGenerator implements AutoCloseable {
    private static IdGenerator instance = null;
    private final AtomicInteger currentId = new AtomicInteger(0);
    private final FileHandler idFile;

    private IdGenerator() throws IOException {
        idFile = new FileHandler(EnvManager.getIndexFile(), true);
        initialize();
    }

    /**
     * Returns instance of {@link IdGenerator}
     *
     * @return {@link IdGenerator}
     * @throws IOException if failed to open index file
     */
    public static synchronized IdGenerator getInstance() throws IOException {
        return instance == null ? instance = new IdGenerator() : instance;
    }

    private void initialize() {
        try {
            String lastIdStr = idFile.read();
            if (lastIdStr != null) currentId.set(Integer.parseInt(lastIdStr));
            else currentId.set(0);
        } catch (NumberFormatException e) {
            idFile.write(Long.toString(currentId.getAndSet(0)));
        }
    }

    /**
     * Generates unique if for a collection element.
     *
     * @return int
     * @throws IOException if failed to read/write from index file
     */
    public int generateId() throws IOException {
        idFile.write(currentId.get() + System.lineSeparator());
        return currentId.incrementAndGet();
    }

    @Override
    public void close() throws IOException {
        idFile.close();
    }
}
