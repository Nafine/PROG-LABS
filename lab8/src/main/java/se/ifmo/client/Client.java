package se.ifmo.client;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.client.util.EnvManager;
import se.ifmo.shared.PacketManager;
import se.ifmo.shared.command.*;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Credentials;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.communication.Router;
import se.ifmo.shared.model.Vehicle;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Credentials credentials;
    private DatagramSocket socket;

    {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(2500);
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

    public boolean login(String login, String password) {
        Credentials credentials = new Credentials(login, password);

        try {
            Callback callback = handleRequest(new Request(new Login(), Collections.emptyList(), credentials));
            if (callback.equals(Callback.successfulLogin())) {
                this.credentials = credentials;
                return true;
            }

            this.credentials = Credentials.empty();
            return false;
        } catch (IOException e) {
            this.credentials = Credentials.empty();
            return false;
        }
    }

    public boolean register(String login, String password) {
        Credentials credentials = new Credentials(login, password);

        try {
            Callback callback = handleRequest(new Request(new Register(), Collections.emptyList(), credentials));
            if (callback.equals(Callback.successfulLogin())) {
                this.credentials = credentials;
                return true;
            }
            this.credentials = Credentials.empty();
            return false;
        } catch (IOException e) {
            this.credentials = Credentials.empty();
            return false;
        }
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

    public Callback forwardUntilSuccess(Command command) {
        return forwardUntilSuccess(command, Collections.emptyList());
    }

    public Callback forwardUntilSuccess(Command command, List<String> args) {
        Callback callback;
        while ((callback = forwardWhileBad(command, args)).equals(Callback.serverDidNotRespond())) ;
        return callback;
    }

    public Callback forwardWhileBad(Command command) {
        return forwardWhileBad(command, Collections.emptyList());
    }

    public Callback forwardWhileBad(Command command, List<String> args) {
        Callback callback;
        while ((callback = forwardCommand(command, args)).equals(Callback.damagedPackets())) ;
        return callback;
    }

    public Callback forwardCommand(Command command) {
        return forwardCommand(command, Collections.emptyList());
    }

    public Callback forwardCommand(Command command, List<String> args) {
        try {
            return handleRequest(new Request(command, args, credentials));
        } catch (IOException e) {
            return new Callback("Error during handling request: "
                    + command + " with arguments " + String.join(" ", args));
        }
    }

    private Callback handleRequest(Request request) throws IOException {
        splitSendRequest(request);
        return receiveCallback();
    }

    private void splitSendRequest(Request request) throws IOException {
        System.out.println("Sent: " + request.command());
        System.out.println("With args: " + String.join(" ", request.args()));
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
                    System.out.println(totalPackets);
                    Callback callback = SerializationUtils.deserialize(receivedData);
                    System.out.println(callback.message() + " " + callback.vehicles().size());
                    return callback;
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Server did not respond");
            return Callback.serverDidNotRespond();
        } catch (SerializationException e) {
            System.out.println("Packets damaged");
            return Callback.damagedPackets();
        }
    }

    @Override
    public void close() {
        socket.close();
    }
}