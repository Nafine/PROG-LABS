package se.ifmo.client;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.client.console.Console;
import se.ifmo.client.console.StandardConsole;
import se.ifmo.server.file.FileHandler;
import se.ifmo.shared.command.ExecuteScript;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.io.IOHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Client implements AutoCloseable {
    int serverPort;
    DatagramSocket socket;
    ByteBuffer buffer = ByteBuffer.allocate(65536);
    Console console = new StandardConsole();
    IOHandler<String> scriptHandler;

    {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(3000);

            Path errorFile = Path.of(System.getenv("ERROR_LOG"));
            System.setErr(new PrintStream(new FileOutputStream(errorFile.toFile())));
        } catch (SocketException e) {
            System.out.println("Unable to create socket");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.err.close();
            System.out.println("ERROR_LOG file was not found, error logging will not be performed.");
        } catch (NullPointerException e) {
            System.err.println("Check ERROR_LOG environment variable, it was not defined.");
        }
    }

    public Client(int serverPort) {
        this.serverPort = serverPort;
    }

    public static void main(String[] args) {
        try (Client client = new Client(8080)) {
            System.out.println("Client initialized");
            client.run();
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }

    public void run() {
        String input;

        while ((input = console.read()) != null) {
            handle(input);
            if (scriptHandler != null) handle(scriptHandler.read());
        }
    }

    private void handle(String input) {
        if (input == null || input.isBlank()) return;

        printCallback(handleRequest(parse(input)));
    }

    private Callback handleRequest(Request request) {
        if (new ExecuteScript().getName().equals(request.command())) {
            File file = Path.of(request.args().get(0)).toFile();

            if (!file.exists()) return new Callback("File not found.");
            if (!file.isFile()) return new Callback("Path is not a file.");
            if (!file.canRead()) return new Callback("Not enough rights to read file.");

            try{
                scriptHandler = new FileHandler(file.toPath(), true);
            } catch (Exception e) {
                System.err.println("Error closing console: " + e.getMessage());
                return new Callback("Can't close console");
            }
        }

        sendRequest(request);
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

    public void sendRequest(Request request) {
        try {
            buffer.clear().put(SerializationUtils.serialize(request)).flip();
            DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.remaining(), InetAddress.getByName("localhost"), serverPort);
            socket.send(packet);
            buffer.clear();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException during sending request: " + e.getMessage());
        } catch (SerializationException e) {
            System.out.println("Failed to deserialize packet: " + e.getMessage());
        }
    }

    public Callback receiveCallback() {
        try {
            DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.remaining(), InetAddress.getByName("localhost"), serverPort);

            socket.receive(packet);
            return SerializationUtils.deserialize(packet.getData());
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + e.getMessage());
            return new Callback("Unknown host.");
        } catch (IOException e) {
            System.err.println("IOException during sending request: " + e.getMessage());
            return new Callback("Error receiving callback.");
        } catch (SerializationException e) {
            System.err.println("Can't deserialize packet: " + e.getMessage());
            return new Callback("Error receiving callback.");
        }
    }

    @Override
    public void close() {
        socket.close();
    }
}
