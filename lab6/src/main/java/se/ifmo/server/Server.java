package se.ifmo.server;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.communication.Router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class Server implements AutoCloseable {
    final int PORT = 8080;
    DatagramChannel channel;
    Selector selector;
    ByteBuffer buffer = ByteBuffer.allocate(65536);

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

    public static void main(String[] args) {
        try (Server server = new Server()) {
            System.out.println("Server is ready for handling requests");
            server.run();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void run() {
        while (true) {
            try {
                if (selector.select() != 0) {
                    for (SelectionKey key : selector.selectedKeys()) {
                        if (key.isReadable()) {
                            processRequest();
                        }
                    }
                    selector.selectedKeys().clear();
                }
            } catch (IOException e) {
                System.err.println("Unable to select");
                System.err.println(e.getMessage());
            }
        }
    }

    private void processRequest() {
        SocketAddress clientAddress = receiveRequest();

        Request request = SerializationUtils.deserialize(buffer.array());

        System.out.println("Server received remain: " + buffer.remaining());

        Callback callback = Router.getInstance().route(request);
        sendCallback(clientAddress, callback);
    }

    private SocketAddress receiveRequest() {
        try {
            buffer.clear();
            SocketAddress clientAddress = channel.receive(buffer);
            buffer.flip();

            return clientAddress;
        } catch (IOException e) {
            System.err.println("Could not receive request");
            System.err.println(e.getMessage());
            return null;
        }
    }

    private void sendCallback(SocketAddress remoteAddress, Callback callback) {
        try {
            buffer.clear().put(SerializationUtils.serialize(callback)).flip();

            System.out.println("Sent Buffer remain: " + buffer.remaining());

            channel.send(buffer, remoteAddress);
            buffer.clear();
        } catch (IOException e) {
            System.err.println("Could not send callback");
            System.err.println(e.getMessage());
        } catch (BufferOverflowException e) {
            System.err.println("Buffer overflow");
            System.err.println(e.getMessage());
        }catch (SerializationException e){
            System.err.println("Could not serialize callback");
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void close() throws IOException {
        CollectionManager.getInstance().save();
        channel.close();
        selector.close();
    }
}
