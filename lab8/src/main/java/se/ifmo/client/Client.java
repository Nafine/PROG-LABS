package se.ifmo.client;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.client.util.EnvManager;
import se.ifmo.shared.PacketManager;
import se.ifmo.shared.command.*;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Credentials;
import se.ifmo.shared.communication.Request;
import se.ifmo.server.communication.Router;
import se.ifmo.shared.model.Vehicle;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class Client implements AutoCloseable {
    private static Client instance;
    private final InetAddress host;
    private final int serverPort;
    private Credentials credentials = Credentials.empty();
    private DatagramSocket socket;

    {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(5000);
        } catch (SocketException e) {
            System.out.println("Unable to initiate client:");
            System.out.println(e.getMessage());
        }
    }

    private Client() {
        this.host = EnvManager.getHost();
        this.serverPort = EnvManager.getPort();
    }

    public static Client getInstance() {
        return instance == null ? instance = new Client() : instance;
    }

    public Callback login(String login, String password) {
        Credentials credentials = new Credentials(login, password);

        Callback callback = handleRequest(new Request(new Login(), Collections.emptyList(), credentials));
        if (callback.equals(Callback.successfulLogin())) {
            this.credentials = credentials;
        }

        return callback;
    }

    public Callback register(String login, String password) {
        Credentials credentials = new Credentials(login, password);


        Callback callback = handleRequest(new Request(new Register(), Collections.emptyList(), credentials));
        if (callback.equals(Callback.successfulLogin())) {
            this.credentials = credentials;
        }

        return callback;
    }

    public String getUsername() {
        return credentials.username();
    }

    public void removeByID(long id) {
        forwardCommand(new RemoveById(), List.of(String.valueOf(id)));
    }

    public void updateByID(long id, Vehicle newVehicle) {
        forwardCommand(new UpdateId(), Stream.of(id, newVehicle.getName(), newVehicle.getCoordinates().getX(),
                        newVehicle.getCoordinates().getY(), newVehicle.getEnginePower(),
                        newVehicle.getCapacity(), newVehicle.getDistanceTravelled(),
                        newVehicle.getFuelType())
                .map(t -> t == null ? "null" : t.toString())
                .collect(Collectors.toList()));
    }

    public long getUID() {
        return Long.parseLong(forwardCommand(new GetUID()).message());
    }

    public CompletableFuture<Callback> forwardCommandAsync(Command command) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return forwardCommand(command);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    public Callback forwardCommand(Command command) {
        return forwardCommand(command, Collections.emptyList());
    }

    public Callback forwardCommand(Command command, List<String> args) {
        return handleRequest(new Request(command, args, credentials));
    }

    private Callback handleRequest(Request request) {
        try {
            splitSendRequest(request);
            return receiveCallback();
        } catch (IOException e) {
            return new Callback("Error during handling request: "
                    + request.command() + " with arguments " + String.join(" ", request.args()));
        }
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
        } catch (SerializationException e) {
            return Callback.damagedPackets();
        }
    }

    @Override
    public void close() {
        socket.close();
    }
}