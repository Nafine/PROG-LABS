package se.ifmo.server;

import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.server.communication.Router;
import se.ifmo.server.db.UserService;
import se.ifmo.server.util.EnvManager;
import se.ifmo.shared.PacketManager;
import se.ifmo.shared.command.Login;
import se.ifmo.shared.command.Register;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.io.console.Console;
import se.ifmo.shared.io.console.StandardConsole;

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
    public static final Logger logger = Logger.getLogger(Server.class.getName());
    private static FileHandler fh;

    static {
        try {
            fh = new FileHandler("ext/log.txt");
        } catch (IOException e) {
            System.out.println("Was not able to open log file: " + e.getMessage());
            logger.log(Level.SEVERE, "Was not able to open log file: ", e);
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
            console.writeln("Failed to launch server.");
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
                executor.execute(this::receiveRequest);
            }
        }
    }

    private void receiveRequest() {
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

            if (request.command().equals(new Login().getName()) && request.command().equals(new Register().getName())
                    && !UserService.getInstance().comparePassword(request.credentials())) {
                splitSendCallback(Callback.wrongCredentials(), clientAddress);
                logger.info(String.format("Wrong credentials from: %s:%s\ncredentials: [%s, %s]", clientAddress.getHostString(), clientAddress.getPort(),
                        request.credentials().username(), request.credentials().password()));
            } else {
                Callback response = Router.getInstance().route(request);
                splitSendCallback(response, clientAddress);
                logger.fine(String.format("Received message from %s:%s\nContains:\n%s", clientAddress.getHostString(), clientAddress.getPort(), request));
            }
        }
    }

    private void splitSendCallback(Callback callback, InetSocketAddress clientAddress) {
        int i = 0;
        for (byte[] packet : PacketManager.splitMessage(SerializationUtils.serialize(callback))) {
            try {
                i++;
                channel.send(ByteBuffer.wrap(packet), clientAddress);
                Thread.sleep(0, 300_000);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error sending packet", e);
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "Thread has been interrupted", e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        logger.fine("Closing server");
        executor.close();
        channel.close();
        selector.close();
    }
}