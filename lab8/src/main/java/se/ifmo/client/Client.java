package se.ifmo.client;

import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.client.command.ClientSideCommands;
import se.ifmo.shared.file.FileHandler;
import se.ifmo.shared.PacketManager;
import se.ifmo.shared.command.ExecuteScript;
import se.ifmo.shared.command.Login;
import se.ifmo.shared.command.Register;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Credentials;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.communication.Router;
import se.ifmo.shared.io.console.Console;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;

import static se.ifmo.shared.PacketManager.BUFFER_SIZE;

/**
 * UDP client implementation using blocking I/O.
 *
 * <p>Key features:
 * <ul>
 *   <li>Fragmented message assembly</li>
 *   <li>Socket-based implementation</li>
 *   <li>Integrated routing system through {@link Router}</li>
 *   <li>Thread-safe packet reassembly</li>
 * </ul>
 */
public class Client implements AutoCloseable, Runnable {

    private final InetAddress host;
    private final int serverPort;
    private final Console console;
    private DatagramSocket socket;


    {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(4500);
        } catch (SocketException e) {
            System.out.println("Unable to initiate client:");
            System.out.println(e.getMessage());
        }
    }

    public Client(Console console, InetAddress host, int serverPort) {
        this.console = console;
        this.host = host;
        this.serverPort = serverPort;
    }

    private Credentials login() {
        Credentials credentials = new Credentials(console.read("Insert your login\n"), console.read("Insert your password\n"));

        try {
            Callback callback = handleRequest(new Request(new Login(), Collections.emptyList(), credentials));
            printCallback(callback);
            if (callback.equals(Callback.successfulLogin())) return credentials;
            return Credentials.empty();
        } catch (IOException e) {
            return Credentials.empty();
        }
    }

    private Credentials register() {
        Credentials credentials = new Credentials(console.read("Insert your login\n"), console.read("Insert your password\n"));

        try {
            Callback callback = handleRequest(new Request(new Register(), Collections.emptyList(), credentials));
            printCallback(callback);
            if (callback.equals(Callback.successfulLogin())) return credentials;
            return Credentials.empty();
        } catch (IOException e) {
            return Credentials.empty();
        }
    }

    private Credentials initUser() {
        return switch (console.read("Enter 'login' if you have account or 'register' otherwise:\n")) {
            case "login" -> login();
            case "register" -> register();
            default -> Credentials.empty();
        };
    }

    /**
     * Starts the client's event processing loop.
     * <p>
     * Infinite input handling loop.
     * </p>
     */
    @Override
    public void run() {
        Credentials credentials;
        while ((credentials = initUser()).equals(Credentials.empty())) ;

        String input;

        while ((input = console.read("> ")) != null) handleInput(input.trim(), credentials);
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
    protected void handleInput(String input, Credentials credentials) {
        if (input == null || input.isBlank()) return;

        try {
            printCallback(handleRequest(makeRequest(input, credentials)));
        } catch (IOException e) {
            System.err.println("Error during handling request:\n" + e.getMessage());
        }
    }

    private Callback handleRequest(Request request) throws IOException {
        if (new ExecuteScript().getName().equals(request.command()))
            return (new ScriptHandler(this)).handleScript(request);

        else if (ClientSideCommands.MAP.containsKey(request.command())) return Router.getInstance().route(request);

        splitSendRequest(request);
        return receiveCallback();
    }

    private Request makeRequest(String prompt, Credentials credentials) {
        String[] parts = prompt.split("\\s+", 2);

        String command = parts[0];

        List<String> args = parts.length > 1 ? Arrays.asList(parts[1].split("\\s+")) : Collections.emptyList();

        return new Request(command, args, credentials);
    }

    private void printCallback(Callback callback) {
        if (callback.message() != null && !callback.message().isBlank()) console.writeln(callback.message());
        if (callback.vehicles() != null && !callback.vehicles().isEmpty())
            callback.vehicles().forEach(vehicle -> console.writeln(vehicle.toString()));
    }

    private void splitSendRequest(Request request) throws IOException {
        for (byte[] packet : PacketManager.splitMessage(SerializationUtils.serialize(request)))
            socket.send(new DatagramPacket(packet, packet.length, host, serverPort));
    }

    private Callback receiveCallback() throws IOException {
        Map<Integer, byte[]> receivedPackets = new HashMap<>();
        byte[] receivedData;

        try {
            while (true) {
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.remaining(), host, serverPort);
                socket.receive(packet);
                int seqNum = buffer.getInt();
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
            return Callback.serverDidNotRespond();
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
            file = Path.of(request.args().getFirst()).toFile();
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
            while (!scriptQueue.isEmpty()) client.handleInput(scriptQueue.poll(), request.credentials());

            runningScripts.remove(file.getAbsolutePath());

            return Router.getInstance().route(request);
        } catch (IOException e) {
            runningScripts.remove(file.getAbsolutePath());
            System.err.println("Error closing file: " + e.getMessage());
            return new Callback("Can't close file");
        }
    }
}