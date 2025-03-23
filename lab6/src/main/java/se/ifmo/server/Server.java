package se.ifmo.server;

import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.PacketManager;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.communication.Router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.logging.Level;

public class Server implements AutoCloseable {
    public static final int BUFFER_SIZE = 1024;
    public static final int PACKET_SIZE = BUFFER_SIZE - 5;
    private static final Map<InetSocketAddress, Map<Integer, byte[]>> clientMessages = new HashMap<>();
    final int PORT = 8080;
    DatagramChannel channel;
    Selector selector;

    {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(PORT));
            try {
                selector = Selector.open();
                channel.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                System.err.println("Could not open selector");
                System.err.println(e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Could not open channel");
            System.err.println(e.getMessage());
        }
    }

    public void run() {
        try {
            while (true) {
                select();
            }
        } catch (IOException e) {
            System.err.println("Error during selection");
            System.err.println(e.getMessage());
        }
    }

    private void select() throws IOException {
        if (selector.select() == 0) return;
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            try {
                if (key.isReadable()) receiveRequest();
            } catch (IOException e) {
                System.err.println("Error reading key");
                System.err.println(e.getMessage());
            }

            keyIterator.remove();
        }
    }

    private void receiveRequest() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);

        if (clientAddress == null) return;
        buffer.flip();

        int packetId = buffer.get();
        int totalPackets = buffer.getInt();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        clientMessages.computeIfAbsent(clientAddress, k -> new HashMap<>()).put(packetId, data);
        if (clientMessages.get(clientAddress).size() == totalPackets) {
            Request request = SerializationUtils.deserialize(PacketManager.assemblePackets(clientMessages.remove(clientAddress)));
            Callback response = Router.getInstance().route(request);
            splitSendCallback(response, clientAddress);
        }
    }

    private void splitSendCallback(Callback callback, InetSocketAddress clientAddress) throws IOException {
        byte[] data = SerializationUtils.serialize(callback);
        int numPackets = (int) Math.ceil((double) data.length / (PACKET_SIZE));
        for (int i = 0; i < numPackets; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            buffer.put((byte) i);
            buffer.putInt(numPackets);
            buffer.put(data, i * PACKET_SIZE, Math.min(PACKET_SIZE, data.length - i * PACKET_SIZE));
            buffer.flip();
            channel.send(buffer, clientAddress);
        }
    }

    @Override
    public void close() throws IOException {
        CollectionManager.getInstance().save();
        channel.close();
        selector.close();
    }
}
