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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static se.ifmo.shared.PacketManager.BUFFER_SIZE;

/**
 * Class which defines server side of a program.
 * Usage: see {@link Server#run()}.
 *
 * <p>
 * Uses {@link Selector} for non-blocking IO, packet receiving and virtual threads for handling user's {@link Request}.
 * Client registration is required for the client's request to be processed. See {@link se.ifmo.client.Client} for more information.
 * </p>
 */
public class Server implements AutoCloseable {
    // Network buffer size optimized for Ethernet MTU (1500 bytes).
    public static final Logger logger = Logger.getLogger(Server.class.getName());
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
    private final Map<InetSocketAddress, Map<Integer, byte[]>> clientMessages = new HashMap<>();
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    boolean isRunning = true;
    DatagramChannel channel;
    Selector selector;
    Console console = new StandardConsole();

    {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(PORT));
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            //Load collection
            CollectionManager.getInstance();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not initialize server", e);
            System.out.println("Failed to launch server.");
        }
        logger.fine(String.format("Server launched, listening on port %d", PORT));
    }

    /**
     * Launch server and start receiving packets from clients.
     */
    public void run() {
        try {
            while (isRunning) {
                select();
                if (console.ready()) handleInput(console.read());
                Thread.sleep(3);
            }
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Error during server loop", e);
        } finally {
            try {
                close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error during shutdown", e);
            }
        }
    }

    private void handleInput(String input) {
        //ну будет время - исправлю
        switch (input) {
            case "exit" -> isRunning = false;
            default -> console.writeln("Unknown command");
        }
    }

    private void select() throws IOException {
        if (selector.selectNow() == 0) return;
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            keyIterator.remove();

            if (key.isReadable()) {
                receiveRequest();
            }
        }
    }

    private void receiveRequest() {
        executor.execute(() -> {

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            InetSocketAddress clientAddress = null;
            try {
                clientAddress = (InetSocketAddress) channel.receive(buffer);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error receiving request", e);
            }

            if (clientAddress == null) return;

            buffer.flip();
            int packetId = buffer.getInt();
            int totalPackets = buffer.getInt();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            clientMessages.computeIfAbsent(clientAddress, k -> new HashMap<>()).put(packetId, data);
            if (clientMessages.get(clientAddress).size() == totalPackets) {
                Request request = SerializationUtils.deserialize(PacketManager.assemblePackets(clientMessages.remove(clientAddress)));
                Callback response = Router.getInstance().route(request);
                splitSendCallback(response, clientAddress);
                logger.fine(String.format("Received message from %s:%s\nContains:\n%s", clientAddress.getHostString(), clientAddress.getPort(), request));
            }
        });
    }

    private void splitSendCallback(Callback callback, InetSocketAddress clientAddress) {
        executor.execute(() -> {
            for (byte[] packet : PacketManager.splitMessage(SerializationUtils.serialize(callback))) {
                try {
                    channel.send(ByteBuffer.wrap(packet), clientAddress);
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Error sending packet", e);
                }
            }
        });
    }

    @Override
    public void close() throws IOException {
        System.out.println("Closing server");
        logger.fine("Closing server");
        executor.close();
        channel.close();
        selector.close();
    }
}