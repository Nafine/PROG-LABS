package se.ifmo.server;

import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.client.io.console.Console;
import se.ifmo.client.io.console.StandardConsole;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.server.util.EnvManager;
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
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UDP server implementation using non-blocking I/O.
 *
 * <p>Key features:
 * <ul>
 *   <li>Fixed-size packet handling ({@value #BUFFER_SIZE} bytes)</li>
 *   <li>Fragmented message assembly</li>
 *   <li>Selector-based I/O multiplexing</li>
 *   <li>Integrated routing system through {@link Router}</li>
 *   <li>Thread-safe packet reassembly</li>
 * </ul>
 */
public class Server implements AutoCloseable {
    // Network buffer size optimized for Ethernet MTU (1500 bytes).
    public static final int BUFFER_SIZE = 1500;
    /*
     * Maximum payload size per packet.
     * Calculated as BUFFER_SIZE - 5 (1 byte packet ID + 4 bytes total packet count).
     */
    public static final int PACKET_SIZE = BUFFER_SIZE - 5;
    public static final Logger logger = Logger.getLogger("se.ifmo.server.Server");
    //Partial message cache for client transmissions.
    private static final Map<InetSocketAddress, Map<Integer, byte[]>> clientMessages = new HashMap<>();
    private static FileHandler fh;

    static {
        try {
            fh = new FileHandler("ext/log.txt");
        } catch (IOException e) {
            System.out.println("Was not able to open log file. " + e.getMessage());
        }

        logger.setUseParentHandlers(false);
        logger.addHandler(fh);
        logger.setLevel(Level.ALL);
        logger.info("Logger initialized");
    }

    final int PORT = EnvManager.getPort();
    boolean isRunning = true;
    DatagramChannel channel;
    Selector selector;
    Console console = new StandardConsole();

    {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(PORT));
            try {
                selector = Selector.open();
                channel.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not open selector", e);
                System.out.println("Failed to launch server.");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not open selector", e);
            System.out.println("Failed to launch server.");
        }

        logger.fine(String.format("Server launched, listening on port %d", PORT));
    }

    /**
     * Starts the server's event processing loop.
     *
     * <p>Implementation notes:
     * <ul>
     *   <li>Infinite selector polling loop</li>
     *   <li>Graceful error handling maintains server availability</li>
     * </ul>
     */
    public void run() {
        try {
            while (isRunning) {
                select();
                if (console.ready()) handleInput(console.read());
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, "Thread has been interrupted", e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during selection", e);
            System.out.println("Error while running.");
        }
        try {
            close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error during shutting down server", e);
        }
    }

    private void handleInput(String input) {
        switch (input) {
            case "exit":
                isRunning = false;
                break;
            case "save":
                CollectionManager.getInstance().save();
                System.out.println("Collection was saved");
                logger.fine("Collection was saved");
                break;
            default:
                console.write(String.format("Unknown command: %s", input));
        }
    }

    private void select() throws IOException {
        if (selector.selectNow() == 0) return;
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            try {
                if (key.isReadable()) receiveRequest();
            } catch (IOException e) {
                logger.log(Level.INFO, "Error reading key", e);
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
            //System.out.printf("Packet has been acquired: %d of %d%n", packetId, totalPackets);
            Request request = SerializationUtils.deserialize(PacketManager.assemblePackets(clientMessages.remove(clientAddress)));
            Callback response = Router.getInstance().route(request);
            splitSendCallback(response, clientAddress);
            logger.fine(String.format("Received message from %s:%s%nWhich contains %n%s", clientAddress.getHostString(), clientAddress.getPort(), request));
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
        System.out.println("Closing server");
        logger.fine("Closing server");
        CollectionManager.getInstance().save();
        channel.close();
        selector.close();
    }
}