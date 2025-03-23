package se.ifmo.client.io.queue;

import se.ifmo.shared.io.IOHandler;

import java.util.LinkedList;
import java.util.Queue;

public class QueueHandler implements IOHandler<String> {
    Queue<String> queue = new LinkedList<>();

    @Override
    public String read() {
        return queue.poll();
    }

    @Override
    public void write(String data) {
        queue.add(data);
    }

    public void insert(String item) {
        queue.add(item);
    }

    public boolean ready() {
        return !queue.isEmpty();
    }

    @Override
    public void close() throws Exception {
        queue.clear();
    }
}
