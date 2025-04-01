package se.ifmo.client;

import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.client.command.ClientSideCommands;
import se.ifmo.client.io.console.Console;
import se.ifmo.server.file.FileHandler;
import se.ifmo.shared.PacketManager;
import se.ifmo.shared.command.ExecuteScript;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.communication.Router;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;

/**
 * UDP client implementation using blocking I/O.
 *
 * <p>Key features:
 * <ul>
 *   <li>Fixed-size packet handling ({@value #BUFFER_SIZE} bytes)</li>
 *   <li>Fragmented message assembly</li>
 *   <li>Socket-based implementation</li>
 *   <li>Integrated routing system through {@link Router}</li>
 *   <li>Thread-safe packet reassembly</li>
 * </ul>
 */
public class Client implements AutoCloseable {
    // Network buffer size optimized for Ethernet MTU (1500 bytes).
    public static final int BUFFER_SIZE = 1500;

    /*
     * Maximum payload size per packet.
     * Calculated as {@code BUFFER_SIZE - 5} (1 byte packet ID + 4 bytes total packet count).
     */
    public static final int PACKET_SIZE = BUFFER_SIZE - 5;
    InetAddress host;
    int serverPort;
    DatagramSocket socket;
    Console console;

    {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(3000);
        } catch (SocketException e) {
            System.out.println("Unable to create socket");
            System.out.println(e.getMessage());
        }
    }

    public Client(Console console, InetAddress host, int serverPort) {
        this.console = console;
        this.host = host;
        this.serverPort = serverPort;
    }

    /**
     * Starts the client's event processing loop.
     * <p>
     * Infinite input handling loop.
     * </p>
     */
    public void run() {
        String input;

        while ((input = console.read("> ")) != null) {
            handle(input.trim());
        }
    }


    /**
     * First of all checks {@code input}: immediately returns if input.isBlank() or input == null.
     *
     * <p>
     * Parses input, calls method to handle parsed user input and prints callback from serve.
     * Handles IOExceptions if needed.
     * </p>
     *
     * @param input user's prompt to console
     */
    protected void handle(String input) {
        if (input == null || input.isBlank()) return;

        try {
            printCallback(handleRequest(parse(input)));
        } catch (IOException e) {
            System.err.println("Error during handling request: " + e.getMessage());
        }
    }

    private Callback handleRequest(Request request) throws IOException {
        if (new ExecuteScript().getName().equals(request.command()))
            return (new ScriptHandler(this)).handleScript(request);

        else if (ClientSideCommands.MAP.containsKey(request.command())) return Router.getInstance().route(request);

        splitSendRequest(request);
        return receiveCallback();
    }

    private Request parse(String prompt) {
        String[] parts = prompt.split("\\s+", 2);

        String command = parts[0];

        List<String> args = parts.length > 1 ? Arrays.asList(parts[1].split("\\s+")) : Collections.emptyList();

        return new Request(command, args);
    }

    private void printCallback(Callback callback) {
        if (callback.message() != null && !callback.message().isBlank()) console.writeln(callback.message());
        if (callback.vehicles() != null && !callback.vehicles().isEmpty())
            callback.vehicles().forEach(vehicle -> console.writeln(vehicle.toString()));
    }

    private void splitSendRequest(Request request) throws IOException {
        byte[] data = SerializationUtils.serialize(request);
        int numPackets = (int) Math.ceil((double) data.length / (PACKET_SIZE));
        for (int i = 0; i < numPackets; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            buffer.put((byte) i);
            buffer.putInt(numPackets);
            buffer.put(data, i * PACKET_SIZE, Math.min(PACKET_SIZE, data.length - i * PACKET_SIZE));
            buffer.flip();
            socket.send(new DatagramPacket(buffer.array(), buffer.remaining(), host, serverPort));
        }
    }

    private Callback receiveCallback() throws IOException {
        Map<Integer, byte[]> receivedPackets = new HashMap<>();
        byte[] receivedData;

        try {
            while (true) {
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.remaining(), host, serverPort);
                socket.receive(packet);
                int seqNum = buffer.get();
                int totalPackets = buffer.getInt();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                receivedPackets.put(seqNum, data);
                if (receivedPackets.size() == totalPackets) {
                    receivedData = PacketManager.assemblePackets(receivedPackets);
                    return SerializationUtils.deserialize(receivedData);
                }
            }
        } catch (SocketTimeoutException e) {
            System.err.println("The server response time has been exceeded");
            return new Callback("Server did not respond");
        }
    }

    @Override
    public void close() {
        socket.close();
    }
}

class ScriptHandler {
    private final static HashSet<String> runningScripts = new HashSet<>();
    private final Client client;

    protected ScriptHandler(Client client) {
        this.client = client;
    }

    protected Callback handleScript(Request request) {
        File file;
        try {
            file = Path.of(request.args().get(0)).toFile();
        } catch (InvalidPathException e) {
            return new Callback("String is not a path.");
        }

        if (!file.exists()) return new Callback("File not found.");
        if (!file.isFile()) return new Callback("Path is not a file.");
        if (!file.canRead()) return new Callback("Not enough rights to read file.");

        if (runningScripts.contains(file.getAbsolutePath())) return new Callback("Script already running.");
        runningScripts.add(file.getAbsolutePath());

        try (FileHandler fileHandler = new FileHandler(file.toPath())) {
            Queue<String> scriptQueue = new LinkedList<>();
            String token;
            while (!(token = fileHandler.read()).isEmpty())
                Collections.addAll(scriptQueue, token.split(System.lineSeparator() + "+"));
            while (!scriptQueue.isEmpty()) client.handle(scriptQueue.poll());

            runningScripts.remove(file.getAbsolutePath());

            return Router.getInstance().route(request);
        } catch (IOException e) {
            runningScripts.remove(file.getAbsolutePath());
            System.err.println("Error closing file: " + e.getMessage());
            return new Callback("Can't close file");
        }
    }
}