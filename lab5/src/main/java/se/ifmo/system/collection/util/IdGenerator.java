package se.ifmo.system.collection.util;

import se.ifmo.system.file.FileHandler;

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

    private final FileHandler fileHandler;
    private final AtomicInteger currentId = new AtomicInteger(0);

    private IdGenerator() throws IOException {
        this.fileHandler = new FileHandler(EnvManager.getIndexFile());
        initialize();
    }

    /**
     * Returns instance of {@link IdGenerator}
     * @return {@link IdGenerator}
     * @throws IOException if failed to open index file
     */
    public static synchronized IdGenerator getInstance() throws IOException {
        return instance == null ? instance = new IdGenerator() : instance;
    }

    private void initialize() {
        try {
            String lastIdStr = fileHandler.read();
            if (lastIdStr != null) currentId.set(Integer.parseInt(lastIdStr));
            else currentId.set(0);
        } catch (NumberFormatException e) {
            fileHandler.write(Long.toString(currentId.getAndSet(0)));
        }
    }

    /**
     * Generates unique if for a collection element.
     * @return int
     * @throws IOException if failed to read/write from index file
     */
    public int generateId() throws IOException {
        currentId.incrementAndGet();
        synchronized (fileHandler) {
            fileHandler.write(Long.toString(currentId.get()));
        }
        return currentId.get();
    }

    @Override
    public void close() throws Exception {
        fileHandler.write("");
        fileHandler.close();
    }
}
