package se.ifmo.client.command.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

class LastElements {
    private final Deque<String> deque;
    private final int maxSize;

    public LastElements(int maxSize) {
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

public class HistoryManager {
    private static HistoryManager instance;
    private final LastElements lastElements = new LastElements(9);

    private HistoryManager() {
    }

    public static HistoryManager getInstance() {
        return instance == null ? instance = new HistoryManager() : instance;
    }

    public void addCommand(String command) {
        lastElements.add(command);
    }

    public List<String> getHistory() {
        return lastElements.toList();
    }
}
