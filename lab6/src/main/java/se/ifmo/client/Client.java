package se.ifmo.client;

import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.client.command.ClientSideCommands;
import se.ifmo.client.io.console.Console;
import se.ifmo.shared.PacketManager;
import se.ifmo.shared.command.ExecuteScript;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.communication.Router;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.*;

public class Client implements AutoCloseable {
    public static final int BUFFER_SIZE = 1024;
    public static final int PACKET_SIZE = BUFFER_SIZE - 5;
    InetAddress host;
    int serverPort;
    DatagramSocket socket;
    ByteBuffer buffer = ByteBuffer.allocate(65536);
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

    public void run() {
        String input;

        while ((input = console.read()) != null) {
            handle(input);
        }
    }

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

    public Callback receiveCallback() throws IOException {
        Map<Integer, byte[]> receivedPackets = new HashMap<>();
        byte[] receivedData;

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
    }

    @Override
    public void close() {
        socket.close();
    }
}
