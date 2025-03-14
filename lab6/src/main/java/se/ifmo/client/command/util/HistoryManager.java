package se.ifmo.client.command.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

class CircularBuffer {
    private final Deque<String> deque;
    private final int maxSize;

    public CircularBuffer(int maxSize) {
        this.maxSize = maxSize;
        this.deque = new ArrayDeque<>(maxSize);
    }

    public void add(String element) {
        if (deque.size() == maxSize) deque.removeFirst();

        deque.addLast(element);
    }

    public List<String> toList() {
        return deque.stream().toList();
    }
}

/**
 * Singleton.
 * <p>
 * Utility class for managing command history.
 * Contains last 9 executed commands.
 * </p>
 */
public class HistoryManager {
    private static HistoryManager instance;
    private final CircularBuffer circularBuffer = new CircularBuffer(9);

    private HistoryManager() {
    }

    /**
     * Get instance of singleton class.
     *
     * @return {@link HistoryManager}
     */
    public static HistoryManager getInstance() {
        return instance == null ? instance = new HistoryManager() : instance;
    }

    /**
     * Add command to a circular buffer.
     *
     * @param command command to append
     */
    public void addCommand(String command) {
        circularBuffer.add(command);
    }

    /**
     * Getter method for a circular buffer.
     *
     * @return list of last nine commands
     */
    public List<String> getHistory() {
        return circularBuffer.toList();
    }
}
